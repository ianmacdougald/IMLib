/* If you find that control voltages are not working correctly, make sure that the ES9 is not filtering out DC offsets.
You can do this using its config tool:
https://www.expert-sleepers.co.uk/webapps/es9_config_tool_1.1.html
*/

ES9 {
	classvar scale = 10.0, div = 5.0;
	classvar <offsets, <server;

	*server_{ | newServer(Server.default) |
		if (newServer.isKindOf(Server)) {
			server = newServer;
		} /* else */ {
			"Cannot assign object not of type Server.".warn;
			nil;
		}
	}


	*initOffsets {
		if (server.isNil) {
			this.server = Server.default;
		};

		if (server.hasBooted) {
			offsets = Array.newClear(16);
			16.do { | bus | this.findOffset(bus) };
		} /* else */ {
			"Server % has not booted".format(server).warn;
		};
	}

	*findOffset { | bus |
		var address, oscFunc;

		address = ("/es9_Offset_"++UniqueID.next).asSymbol;
		oscFunc = oscFunc = OSCFunc.new({ | msg |
			offsets[bus] = msg[3];
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
		var sig;
		bus = bus.clip(0, offsets.size - 1);
		sig = SoundIn.ar(bus, scale);
		sig = sig - try { offsets[bus] }{ ES9.initOffsets; 0 };
		^(sig.clip(0.0, div) / div);
	}
}