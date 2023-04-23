/* If you find that control voltages are not working correctly, make sure that the ES9 is not filtering out DC offsets.
You can do this using its config tool:
https://www.expert-sleepers.co.uk/webapps/es9_config_tool_1.1.html
*/

ES9 {
	classvar storageKey = '__ES9Offsets__';
	classvar scale = 10.0, div = 5.0;
	classvar offsets, <server;
	classvar condition;

	*server_{ | newServer(Server.default) |
		if (newServer.isKindOf(Server)) {
			server = newServer;
		} /* else */ {
			"Cannot assign object not of type Server.".warn;
			nil;
		}
	}

	*postOffsets {
		format("ES9 Offsets:\n\t%", offsets).postln;
	}

	*initOffsets {
		if (server.isNil) {
			this.server = Server.default;
		};

		if (server.hasBooted) {
			fork {
				var size = 14;
				condition = Condition.new;
				offsets = Array.newClear(size);
				size.do { | bus |
					this.findOffset(bus);
					condition.hang;
				};
				this.storeOffsets;
				this.postOffsets;
			};
		} /* else */ {
			"Server % has not booted".format(server).warn;
		};
	}

	*storeOffsets {
		IMStorage.add(storageKey -> offsets);
	}

	*offsets {
		offsets = offsets ? IMStorage.at(storageKey).collect(_.asFloat);
		^offsets;
	}

	*findOffset { | bus |
		var address, oscFunc;

		address = ("/es9_Offset_"++UniqueID.next).asSymbol;
		oscFunc = OSCFunc.new({ | msg |
			offsets[bus] = msg[3];
			condition.unhang;
			oscFunc.free;
		}, address);
		//synth
		play {
			var impulse = Impulse.kr(0);
			var sig = SoundIn.ar(bus, scale);
			sig = Latch.ar(sig, impulse);
			SendReply.kr(impulse, address, sig);
			FreeSelf.kr(impulse);
			Silent.ar;
		};
	}

	*ar { | bus(0), scaleMin(0.0), scaleMax(1.0), mul(1.0), add(0) |
		var sig;

		if (this.offsets.isNil) {
			Error("No DC offsets stored from ES9. Try unplugging all jacks from ES9 inputs running the following: ES9.initOffsets;").throw;
		};

		bus = bus.clip(0, offsets.size - 1);
		sig = SoundIn.ar(bus, scale);
		sig = sig - offsets[bus];
		sig = (sig.clip(0.0, div) / div);
		sig = sig.clip(scaleMin, scaleMax);
		sig = sig.linlin(scaleMin, scaleMax, 0.0, 1.0);
		^sig.madd(mul, add);
	}

	*kr { | bus(0), scaleMin(0.0), scaleMax(1.0), mul(1.0), add(0) |
		^A2K.kr(this.ar(bus, scaleMin, scaleMax, mul, add));
	}
}

ES9Bi : ES9 {
	*ar { | bus(0), scaleMin(-1.0), scaleMax(1.0), mul(1.0), add(0) |
		var sig;

		if (this.offsets.isNil) {
			Error("No DC offsets stored from ES9. Try unplugging all jacks from ES9 inputs running the following: ES9.initOffsets;").throw;
		};

		bus = bus.clip(0, offsets.size - 1);
		sig = SoundIn.ar(bus);
		sig = (sig - (offsets[bus] / 2)) * scale;
		sig = (sig.clip(div.neg, div) / div);
		sig = sig.clip(scaleMin, scaleMax);
		sig = sig.linlin(scaleMin, scaleMax, -1.0, 1.0);
		^sig.madd(mul, add);
	}

	*kr { | bus(0), scaleMin(-1.0), scaleMax(1.0), mul(1.0), add(0) |
		^A2K.kr(this.ar(bus, scaleMin, scaleMax, mul, add));
	}
}
