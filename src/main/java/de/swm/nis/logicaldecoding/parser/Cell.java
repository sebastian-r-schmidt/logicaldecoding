package de.swm.nis.logicaldecoding.parser;

/**
 * Represents a single Cell inside a database: The entry inside a column at a specific Row.
 * Contains name, type and value information.
 * @author Schmidt.Sebastian2
 *
 */
public class Cell {

	public enum Type {
		text, integer, date, geometry, real
	};

	private String name;
	private String value;
	private Type type;



	public Cell(Cell.Type type, String name, String value) {
		this.value = value;
		this.type = type;
		this.name = name;
		unquoteStrings();
	}



	public Cell(String type, String name, String value) {
		this.name = name;
		this.type = Type.valueOf(type);
		this.value = value;
		unquoteStrings();
	}


	private void unquoteStrings() {
		if (!value.equals("null")) {
			if (this.type == Type.text || this.type == Type.date || this.type == Type.geometry) {
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



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
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
