+ IMIncrementer {
	/**newModularTake { | mkdir(false) |
	var date, folder;

	date = Date.getDate;

	try { folder = date.format("%B%d").toLower.modular2022 }{
	"Method .modular2022 not found.".warn;
	"Try running: \"String.imKnow_(true)\" and setting .modular2022_ to desired destination.".warn;
	^this;
	};

	if (mkdir) {
	folder.mkdir
	};

	^this.new(format("modular_%_take0.wav", date.format("%y_%m%d")), folder);
	}date.format("%y_%m%d")

	*newSCTake { | mkdir(false) |
	var date, folder;

	date = Date.getDate;

	try { folder = date.format("%B%d").toLower.sc2022 }{
	"Method .sc2022 not found.".warn;
	"Try running: \"String.imKnow_(true)\" and setting .modular2022_ to desired destination.".warn;
	^this;
	};

	if (mkdir) {
	folder.mkdir
	};

	^this.new(format("sc_%_take0.wav", date.format("%y_%m%d")), folder);
	}
	*/
	*newSoundsTake { | type("modular"), mkdir(false) |
		var date, year, folder;
		var method = \sounds;

		date = Date.getDate;
		year = date.format("%Y");

		folder = type+/+year+/+date.format("%m-%B%d").toLower;

		try { folder = folder.perform(method) } {
			method = method.asString;
			"Method .% not understood".format(method).warn;
			"Try running: \"String.imKnow_(true)\" and setting %_ to desired destination"
			.format(method).warn;
			^this;
		};

		if (mkdir) {
			folder.mkdir
		};

		^this.new("%-%-take0.wav".format(type, date.format("%Y-%m%d")), folder);
	}

	*newModularTake { | mkdir(false) |
		^this.newSoundsTake("modular", mkdir);
	}

	*newSCTake { | mkdir(false) |
		^this.newSoundsTake("sc", mkdir);
	}
}
