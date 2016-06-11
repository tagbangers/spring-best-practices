package practice;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Ogawa, Takeshi
 */
@Entity
@Table(name = "todo")
@Getter
@Setter
public class Todo implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Lob
	private String title;

	private boolean done;
}
