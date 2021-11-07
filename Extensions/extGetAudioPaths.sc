+ String {
	isValidAudioPath { | input |
		^this.class.validAudioPaths
		.find([PathName(input).extension]).notNil;
	}

	getAudioPaths {
		var strings = [];
		this.do{ | item, index |
			strings = strings++item.getAudioPaths;
		};
		^strings.as(this.class);
	}

	getBuffers { ^this.getAudioPaths.collect(_.asBuffer) }

	getAudioPaths {
		var strings = [];
		this.do{ | item, index |
			strings = strings++item.getAudioPaths;
		};
		^strings.as(this.class);
	}

	normalizePathAudio { | level(1.0), server(Server.default),
		sampleFormat("int24") |
		var buffer, pathname = PathName(this);
		if(level <= 0.0, {level = level.dbamp});
		Buffer.read(server, this, action: { | buffer |
			buffer.normalize(level);
			File.delete(this);
			buffer.write(
				this,
				pathname.extension,
				sampleFormat,
			);
			buffer.free;
		});
	}

	asBuffer { | server(Server.default), startFrame(0),
		numFrames(-1), action, bufnum |
		^Buffer.read(server, this, startFrame,
			numFrames, action, bufnum);
	}

	getBuffers { | server, startFrame, numFrames, action |
		^this.getAudioPaths.collect(
			_.asBuffer(server, startFrame, numFrames, action)
		);
	}

	*validAudioPaths {
		^[
			"oog",
			"mp3",
			"wav",
			"flac",
			"aiff"
		];
	}
}

+ PathName {
	getAudioPaths { ^fullPath.getAudioPaths }
}

+ Collection {
	getAudioPaths {
		var strings = [];
		this.do{ | item, index |
			strings = strings++item.getAudioPaths;
		};
		^strings.as(this.class);
	}
}
