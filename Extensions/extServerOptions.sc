+ ServerOptions {
	*newIanBasic { | inputs(2), outputs(2), outDevice, inDevice |
		^this.new.toIanBasic(inputs, outputs, outDevice, inDevice);
	}

	*tascam12 {
		^this.newIanBasic(12, 10);
	}

	*komplete6 {
		^this.newIanBasic(4, 4);
	}

	*es9 {
		^this.newIanBasic(16, 16, "ES-9");
	}

	toIanBasic { | inputs(2), outputs(2), outDevice, inDevice |
		inDevice = inDevice ? outDevice;

		this.inDevice = inDevice;
		this.outDevice = outDevice;
		this.sampleRate = 48e3;
		this.memSize = 2.pow(19);
		this.numInputBusChannels = inputs;
		this.numOutputBusChannels = outputs;
	}

	toTascam12 {
		this.toIanBasic(12, 10);
	}

	toKomplete6 {
		this.toIanBasic(6, 6);
	}

	toES9 {
		this.toIanBasic(16, 16, "ES-9");
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
