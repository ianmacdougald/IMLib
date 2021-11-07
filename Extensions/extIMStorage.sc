+ IMStorage {
	*sccodeConfig {
		var date = Date.getDate;
		this.add(\sccode -> "~/Documents/sc/sccode".standardizePath);
		(2016..date.year).do {
			| year |
			var key = format("sc%", year).asSymbol;
			var subkey = { | months("ws") |
				format("%%", key, months).asSymbol;
			};
			this.add(key -> (this.at(\sccode)+/+year.asString));
			3.do { | index |
				switch(index)
				{ 0 }{
					this.add(subkey.value("ws") -> (this.at(key)+/+"winter_spring"));
				}
				{ 1 }{
					this.add(subkey.value("summer") -> (this.at(key)+/+"summer"));
				}
				{ 2 }{
					this.add(subkey.value("fall") -> (this.at(key)+/+"fall"));
				};
			};
		};
	}
}
