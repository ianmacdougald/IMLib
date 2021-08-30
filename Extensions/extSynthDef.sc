+ SynthDef{
	*ianWrapper{ | sig(SinOsc.ar(\freq.kr(400), \phase.kr(0))), timescale(\timescale.kr(1)) |
		^SynthDef.wrap({
			var env = EnvGen.ar(
				Env.perc(
					\atk.kr(0),
					\release.kr(1),
					1,
					\curve.kr(-4)
				),
				timeScale: timescale,
				doneAction: Done.freeSelf
			);
			Pan2.ar(
				sig * env * \ampDb.kr(-12).dbamp,
				\pan.kr(0)
			);
		});
	}

	*ianFilterWrapper{ | sig, freq(\freq.kr(400)), timescale(\timescale.kr(1)), filterClass(RLPF), qArg(\rq.kr(0.2)) |

		if (sig.isNil) {
			Error("Input must be a valid signal").throw;
		};

		^SynthDef.wrap({
			var lo = \ffRLo.kr(1.0);
			var hi = \ffRHi.kr(8.0);
			var ffEnv = EnvGen.ar(
				Env.perc(
					\ffAtk.kr(0),
					\ffRelease.kr(1),
					1,
					\ffCurve.kr(-4)
				),
				timeScale: timescale,
				doneAction: Done.none
			);
			ffEnv = (lo + (ffEnv * (hi - lo))).abs;
			filterClass.ar(
				sig,
				(freq * ffEnv).clip(20.0, SampleRate.ir * 0.45),
				qArg
			);
		});

	}

	*ianFMOperatorWrapper{| n(1), freq, timescale |
		if (n.isNil.not) {
			n = n.asInteger;
		}/*ELSE*/{
			n = "";
		};

		^SynthDef.wrap({
			var atk = format("op%Atk", n).asSymbol.kr(0);
			var release = format("op%Release", n).asSymbol.kr(1);
			var curve = format("op%Curve", n).asSymbol.kr(-4);
			var env = EnvGen.ar(Env.perc(
				atk, release, 1, curve
			),
			timeScale: timescale,
			doneAction: Done.none
			);
			var opFreqRatio = format("op%FreqRatio", n).asSymbol.kr(1.0);
			var op = SinOsc.ar(freq * opFreqRatio);
			var amp = format("op%Amp", n).asSymbol.kr(0);
			op * env * amp;
		});
	}

	*ianFMOpArrayWrapper{|n(1), freq, timescale|
		^n.collect({|index|
			this.ianFMOperatorWrapper(index, freq, timescale);
		});
	}
}
