package personnel;

import java.util.Date;

import lombok.Data;

@Data
public class Certification {
	private int dbID;
	private String name;
	private Date recieved;
	private Date expiration;
	private int hours;
}
