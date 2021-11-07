+ ServerOptions {
	*newIanBasic { | inputs(2), outputs(2) |
		var options = this.new;
		options.sampleRate = 48e3;
		options.memSize = 2.pow(19);
		options.numInputBusChannels = inputs;
		options.numOutputBusChannels = outputs;
		^options;
	}

	*tascam12 {
		^this.newIanBasic(12, 10);
	}

	*komplete6 {
		^this.newIanBasic(4, 4);
	}

	*es9 {
		^this.newIanBasic(16, 16);
	}

	toIanBasic { | inputs(2), outputs(2) |
		this.sampleRate_(48e3).memSize_(2.pow(19))
		.numInputBusChannels_(inputs)
		.numOutputBusChannels_(outputs);
	}

	toTascam12 {
		this.toIanBasic(12, 10);
	}

	toKomplete6 {
		this.toIanBasic(6, 6);
	}

	toES9 {
		this.toIanBasic(16, 16);
	}
}

+ Server {
	bootToKomplete6 { | action({}) |
		this.options.toKomplete6;
		this.waitForBoot(action);
	}

	rebootToKomplete6 { | action({}) |
		this.quit;
		this.bootToKomplete6(action);
	}

	bootToTascam12 { | action({}) |
		this.options.toTascam12;
		this.waitForBoot(action);
	}

	rebootToTascam12 { | action({}) |
		this.quit;
		this.bootToTascam12(action);
	}

	bootToES9 { | action({}) |
		this.options.toES9;
		this.waitForBoot(action);
	}

	rebootToES9 { | action({}) |
		this.quit;
		this.bootToES9(action);
	}
}
