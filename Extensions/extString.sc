+ String {

	asBuffer { | server(Server.default), startFrame(0),
		numFrames(-1), action, bufnum |
		^Buffer.read(server, this, startFrame,
			numFrames, action, bufnum);
	}

}
