IMStorage {
	classvar dictionary, path;

	*write { | item |
		var fd = File.open(path, "w+");
		fd.write(item.asYAMLString);
		fd.close;
	}

	*initClass {
		Class.initClassTree(Main);
		Class.initClassTree(Quarks);
		Class.initClassTree(Dictionary);
		Class.initClassTree(Collection);
		path = Main.packages.asDict.at('IMLib')
		+/+format("%.yaml", this.name);
		this.getDictionary;
	}

	*getDictionary {
		dictionary = path.parseYAMLFile ?? { Dictionary.new };
		dictionary = dictionary.withSymbolKeys;
	}

	*add { | association |
		dictionary.add(association);
		this.write(dictionary);
	}

	*at { | key | ^dictionary[key] }

	*removeAt { | key |
		var object = dictionary.removeAt(key);
		this.write(dictionary);
		^object;
	}

	*keys { ^dictionary.keys }
}

+ String {
	*imKnow_{ | bool(true) |
		if(bool.isKindOf(Boolean).not){
			Error("Can only set to Boolean").throw;
		};
		IMStorage.add('__ENABLE_STRING_PSEUDOS__' -> bool);
	}

	*imKnow {
		^(IMStorage.at('__ENABLE_STRING_PSEUDOS__') ? false);
	}

	fromIMStorage { | key |
		var toPrepend = IMStorage.at(key);
		toPrepend !? { ^(toPrepend+/+this) };
		^this;
	}

	doesNotUnderstand { | selector ... args |
		var bool = IMStorage.at('__ENABLE_STRING_PSEUDOS__');
		if(bool.notNil and: { try { bool.interpret }{ bool } }){
			if(selector.isSetter){
				selector = selector.asGetter;
				IMStorage.add(selector -> args[0]);
			};
			^this.fromIMStorage(selector);
		};
		^this.superPerformList(\doesNotUnderstand, selector, args);
	}
}
