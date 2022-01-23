+ SynthDef {

	*ianWrapper { | sig(SinOsc.ar(\freq.kr(400), \phase.kr(0))), timescale(\timescale.kr(1)) |
		var env;

		if (sig.isNil) {
			Error("Input must be a valid UGen").throw;
		};

		env = Env.perc(
			\atk.kr(0),
			\release.kr(1),
			1,
			\curve.kr(-4)
		).ar(Done.freeSelf, 1, timescale);

		sig = sig * env * \ampDb.kr(-6).dbamp;

		^Pan2.ar(sig, \pan.kr(0));
	}

	*ianFilterWrapper { | sig, freq(\freq.kr(400)),
		timescale(\timescale.kr(1)), filterClass(RLPF), qArg(\rq.kr(0.2)) |

		var lo, hi, ffEnv;

		if (sig.isNil) {
			Error("Input must be a valid UGen").throw;
		};

		lo = \ffRLo.kr(1.0);
		hi = \ffRHi.kr(8.0);

		ffEnv = Env.perc(
			\ffAtk.kr(0),
			\ffRelease.kr(1),
			1,
			\ffCurve.kr(-4)
		).ar(Done.freeSelf, 1, timescale);

		ffEnv = (lo + (ffEnv * (hi - lo))).abs;

		^filterClass.ar(
			sig,
			(freq * ffEnv).clip(20.0, SampleRate.ir * 0.45),
			qArg
		);

	}

	*ianFMOperatorWrapper { | n(1), freq, timescale |
		var atk, release, curve, opFreqRatio, amp, env, op;

		if (n.isNil.not) {
			n = n.asInteger;
		}/*ELSE*/{
			n = "";
		};

		atk = format("op%Atk", n).asSymbol.kr(0);
		release = format("op%Release", n).asSymbol.kr(1);
		curve = format("op%Curve", n).asSymbol.kr(-4);
		opFreqRatio = format("op%FreqRatio", n).asSymbol.kr(1.0);
		amp = format("op%Amp", n).asSymbol.kr(0);

		env = Env.perc(atk, release, 1, curve).ar(Done.freeSelf, 1, timescale);
		op = SinOsc.ar(freq * opFreqRatio);

		^(op * env * amp);
	}

	*ianFMOpArrayWrapper { | n(1), freq, timescale |
		^n.collect { | index |
			this.ianFMOperatorWrapper(index, freq, timescale);
		};
	}
}
