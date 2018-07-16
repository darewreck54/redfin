package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FoodTruck {
	private String location;
	private String start24;
	private String end24;
	private String locationdesc;
	private String optionaltext;
	private String applicant;
	private String starttime;
	private String endtime;
	private String dayofweekstr;

	public String getDayofweekstr() {
		return dayofweekstr;
	}
	public void setDayofweekstr(String dayofweekstr) {
		this.dayofweekstr = dayofweekstr;
	}
	public String getApplicant() {
		return applicant;
	}
	public void setApplicant(String applicant) {
		this.applicant = applicant;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getStart24() {
		return start24;
	}
	public void setStart24(String start24) {
		this.start24 = start24;
	}
	public String getEnd24() {
		return end24;
	}
	public void setEnd24(String end24) {
		this.end24 = end24;
	}
	public String getLocationdesc() {
		return locationdesc;
	}
	public void setLocationdesc(String locationdesc) {
		this.locationdesc = locationdesc;
	}

	
	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public String getOptionaltext() {
		return optionaltext;
	}
	public void setOptionaltext(String optionaltext) {
		this.optionaltext = optionaltext;
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.applicant + "\t" + this.location);
		/*
		sb.append(" - " + this.location);
		sb.append(" - " + this.starttime + " to " + this.endtime + " on " + this.dayofweekstr);
		if(this.optionaltext != null) {
			sb.append("\n\t" + this.optionaltext);
		}
		*/
		return sb.toString();
	}

}
