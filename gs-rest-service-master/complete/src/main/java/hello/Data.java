package hello;

public class Data {

	private String pname;
    private String tname;
    private String aname;
    private String avalue;
    private String msgs;
    

    public Data(String pname, String tname, String aname, String avalue, String msgs) {
        this.pname = pname;
        this.tname = tname;
        this.aname = aname;
        this.avalue = avalue;
        this.msgs = msgs;
    }
    

	public String getMsgs() {
		return msgs;
	}

	public void setMsgs(String msgs) {
		this.msgs = msgs;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getTname() {
		return tname;
	}

	public void setTname(String tname) {
		this.tname = tname;
	}

	public String getAname() {
		return aname;
	}

	public void setAname(String aname) {
		this.aname = aname;
	}

	public String getAvalue() {
		return avalue;
	}

	public void setAvalue(String avalue) {
		this.avalue = avalue;
	}

}
