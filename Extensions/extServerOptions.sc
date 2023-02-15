+ ServerOptions {
	*newIanBasic { | ... args |
		Platform.case(\linux, { args = args[0..1] });
		^this.new.performList(\toBasicIan, args);
	}

	/**tascam12 {
	^this.newIanBasic(12, 10, "Tascam12");
	}

	*komplete6 {
	^this.newIanBasic(4, 4, "Komplete6");
	}

	*es9 {
	^this.newIanBasic(16, 16, "ES-9");
	}*/

	toIanBasic { | inputs(2), outputs(2), outDevice, inDevice |
		inDevice = inDevice ? outDevice;

		this.numInputBusChannels = inputs;
		this.numOutputBusChannels = outputs;

		this.sampleRate = 48e3;
		this.memSize = 2.pow(19);

		Platform.case(
			\osx, {
				inDevice = inDevice ? outDevice;
				this.inDevice = inDevice;
				this.outDevice = outDevice;
			},
			\windows, {
				inDevice = inDevice ? outDevice;
				this.inDevice = inDevice;
				this.outDevice = outDevice;
			}
		);
	}

	toBasicIan { | ... args |
		Platform.case(\linux, { args = args[0..1] });
		this.numInputBusChannels = args[0];
		this.numOutputBusChannels = args[1];

		this.sampleRate = 48e3;
		this.memSize = 2.pow(19);

		if (args.size > 2) {
			this.outDevice = args[2];
			try { this.inDevice = args[3] }{
				this.inDevice = args[2];
			};
		};
	}

	toTascam12 {
		this.toIanBasic(12, 10, "Model 12");
	}

	toKomplete6 {
		this.toIanBasic(6, 6, "Komplete6");
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
