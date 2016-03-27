/*
Copyright (c) 2016 Sebastian Schmidt

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package de.swm.nis.logicaldecoding.parser.domain;

/**
 * Represents a single Cell inside a database: The entry inside a column at a specific Row.
 * Contains name, type and value information.
 * @author Schmidt.Sebastian2
 *
 */
public class Cell {

	public enum Type {
		text(true, true), 
		varchar(true, true), 
		character(true, false), 
		bool(false, false), 
		integer(false, false), 
		bigint(false, false), 
		real(false, false), 
		doubleprec(false, false), 
		numeric(false, false), 
		date(true, true), 
		timestamp(true, false),
		timestamptz(true, false),
		interval(true, false), 
		geometry(true, true), 
		json(false, false), 
		jsonb(false, false), 
		tsvector(true, false), 
		uuid(true, false);
		
		private boolean quotedInJson;
		private boolean quotedInInput;
		
		private Type(boolean quotedInJson, boolean quotedInInput) {
			this.quotedInJson = quotedInJson;
			this.quotedInInput = quotedInInput;
		}
		
		boolean isQuotedInJson() {
			return quotedInJson;
		}
		
		boolean isQuotedInInput() {
			return quotedInInput;
		};
	};

	private String name;
	private String value;
	private Type type;


	public Cell() {
	}

	public Cell(Cell.Type type, String name, String value) {
		this.value = value;
		this.type = type;
		this.name = name;
		unquoteStrings();
	}



	public Cell(String type, String name, String value) {
		this.name = name;
		setType(type);
		this.value = value;
		unquoteStrings();
	}


	private void unquoteStrings() {
		if (!value.equals("null")) {
			if (this.type.isQuotedInInput()) {
				int length = value.length();
				value = value.substring(1, length - 1);
			}
		}
	}



	public String getValue() {
		return value;
	}



	public void setValue(String value) {
		this.value = value;
	}



	public Type getType() {
		return type;
	}



	public void setType(Type type) {
		this.type = type;
	}
	
	public void setType(String typeAsString) {
		switch(typeAsString) 
		{
			case "boolean": {
				this.type = Type.valueOf("bool");
				break;
			}
			case "double precision": {
				this.type = Type.valueOf("doubleprec");
				break;
			}
			case "character varying": {
				this.type = Type.valueOf("varchar");
				break;
			}
			case "timestamp without time zone": {
				this.type = Type.valueOf("timestamp");
				break;
			}
			case "timestamp with time zone": {
				this.type = Type.valueOf("timestamptz");
				break;
			}
			default:
				this.type = Type.valueOf(typeAsString);
		}
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}

	
	public String getJson() {
		String jsonKey = new String("\"" + getName() + "\": ");
		String jsonValue = new String(getValue());
		if (getType().isQuotedInJson()) {
			jsonValue = new String("\"" + getValue() + "\"");
		}
		if (getValue().equals("null")) {
			jsonValue = "null";
		}
		return jsonKey + jsonValue;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cell other = (Cell) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type != other.type)
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}



	@Override
	public String toString() {
		return "Cell [name=" + name + ", type=" + type + ", value=" + value + "]";
	}

}
