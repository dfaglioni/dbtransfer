package pt.evolute.dbtransfer.db.beans;

import pt.evolute.utils.string.StringPlainer;

public class Name {
	public final String saneName;
	public final String originalName;

	public Name(String original) {
		originalName = original;
		String sane = original;
		if (sane.contains(" ")) {
			sane = sane.replace(' ', '_');
		}
		if (sane.contains(".")) {
			sane = sane.replace('.', '_');
		}
		if (sane.contains("\"")) {
			sane = sane.replace('\"', ' ').trim();
		}
		saneName = StringPlainer.convertString(sane);
	}

	@Override
	public String toString() {
		return saneName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((saneName == null) ? 0 : saneName.hashCode());
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
		Name other = (Name) obj;
		if (saneName == null) {
			if (other.saneName != null)
				return false;
		} else if (!saneName.equals(other.saneName))
			return false;
		return true;
	}

	

	
}
