/* If you find that control voltages are not working correctly, make sure that the ES9 is not filtering out DC offsets.
You can do this using its config tool:
https://www.expert-sleepers.co.uk/webapps/es9_config_tool_1.1.html
*/

ES9 {
	classvar storageKey = '__ES9Offsets__';
	classvar scale = 10.0, div = 5.0;
	classvar <offsets, <server;
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
				this.postOffsets;
			};
		} /* else */ {
			"Server % has not booted".format(server).warn;
		};
	}

	*storeOffsets {
		IMStorage.add(storageKey -> offsets);
	}

	*retrieveOffsets {
		offsets = IMStorage.at(storageKey).collect(_.asFloat);
		this.postOffsets;
	}

	*findOffset { | bus |
		var address, oscFunc;

		address = ("/es9_Offset_"++UniqueID.next).asSymbol;
		oscFunc = oscFunc = OSCFunc.new({ | msg |
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
			EnvGen.kr(
				Env([0], [0]),
				impulse,
				doneAction: Done.freeSelf
			);
			Silent.ar;
		};
	}

	*ar { | bus(0) |
		var sig, offset;
		bus = bus.clip(0, offsets.size - 1);

		if (offsets.isNil) {
			this.retrieveOffsets;
		};

		try { offset = offsets[bus] };

		sig = SoundIn.ar(bus, scale);
		sig = sig - (offset ? 0);
		^(sig.clip(0.0, div) / div);
	}
}