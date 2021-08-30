IMIncrementer {
	var fileName, <folder, <extension, i = 0;

	*new { | fileName("some_file.wav"), folder("~/".standardizePath) |
		^super.new
		.folder_(folder)
		.fileName_(fileName)
	}

	folder_{ | newFolder |
		folder = newFolder.asString;
	}

	fileName_{ | newName |
		//Get the new extension.
		extension = PathName(newName).extension;
		//If there isn't one, use "wav" as default.
		if(extension.isEmpty){ extension = "wav" };
		//Get the file name without the extension for incrementing.
		newName = newName[0..(newName.size - 1 - extension.size - 1)];
		//If there is a number at the end of the fileName, strip it.
		fileName = newName.noEndNumbers;
		//Offset the incrementer by that number (0 by default).
		i = newName.endNumber;
	}

	fileName { ^(fileName++"."++extension) }

	fullPath { 
		^(folder+/+fileName++i++"."++extension);
	}

	increment { 
		while({ this.fullPath.exists }){ 
			i = i + 1;
		}; 
		^this.fullPath;
	}

	decrement { 
		var tmp;  
		i = i - 1; 
		if( i < 0 ){ i = 0 };
		tmp = this.fullPath;
		i = i + 1; 
		^tmp;
	}

	reset { i = 0; }

	extension_{ | newExtension("wav") |
		extension = newExtension.split($.).last;
	}
}
