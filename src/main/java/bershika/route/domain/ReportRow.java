package bershika.route.domain;

import java.util.ArrayList;
import java.util.List;

public class ReportRow {
	
	private String destInfo;
	private List<ReportEntry> entries = new ArrayList<ReportEntry>(3);
	public String getDestInfo() {
		return destInfo;
	}
	public void setDestInfo(String destInfo) {
		this.destInfo = destInfo;
	}
	public ReportEntry[] getEntries() {
		return entries.toArray(new ReportEntry[entries.size()]);
	}
	public void setEntries(List<ReportEntry> entries) {
		this.entries = entries;
	}
}
