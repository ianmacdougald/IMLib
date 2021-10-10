FieldsteelReverb{
	//From the great Eli Fieldsteel's tutorials or lectures.
	//From somewhere.
	*ar{ | in, ffreq(\freq.kr(1500)), decay(\decay.kr(4)) |
		var sig = LPF.ar(in, ffreq);
		sig = DelayN.ar(sig, 0.03, 0.03);
		sig = CombN.ar(sig, 0.1, ({ Rand(0.01, 0.099) }!32), decay);
		sig = Splay.ar(sig);
		sig = LeakDC.ar(sig);
		5.do{
			sig = AllpassN.ar(sig, 0.1, ({ Rand(0.01, 0.099) }!2), decay * 3 / 4);
		};
		sig = LPF.ar(sig, ffreq);
		^sig;
	}

}