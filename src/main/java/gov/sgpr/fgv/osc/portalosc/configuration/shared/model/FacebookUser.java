package gov.sgpr.fgv.osc.portalosc.configuration.shared.model;

public class FacebookUser extends DefaultUser {
	/**
	 * 
	 */
	private static final long serialVersionUID = -913721876438285246L;

	private String uid;
	private String smallPictureUrl;
	private String accessToken;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getSmallPictureUrl() {
		return smallPictureUrl;
	}

	public void setSmallPictureUrl(String smallPictureUrl) {
		this.smallPictureUrl = smallPictureUrl;
	}

	public boolean isDefaultUser() {
		if (this.getCpf() > 0)
			return true;
		else
			return false;
	}
	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((accessToken == null) ? 0 : accessToken.hashCode());
		result = prime * result
				+ ((smallPictureUrl == null) ? 0 : smallPictureUrl.hashCode());
		result = prime * result + ((uid == null) ? 0 : uid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		FacebookUser other = (FacebookUser) obj;
		if (accessToken == null) {
			if (other.accessToken != null)
				return false;
		} else if (!accessToken.equals(other.accessToken))
			return false;
		if (smallPictureUrl == null) {
			if (other.smallPictureUrl != null)
				return false;
		} else if (!smallPictureUrl.equals(other.smallPictureUrl))
			return false;
		if (uid == null) {
			if (other.uid != null)
				return false;
		} else if (!uid.equals(other.uid))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FacebookUser [uid=" + uid + ", smallPictureUrl="
				+ smallPictureUrl + ", accessToken=" + accessToken + "]";
	}
	


}
