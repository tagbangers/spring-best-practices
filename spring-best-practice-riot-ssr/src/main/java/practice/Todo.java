package practice;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Ogawa, Takeshi
 */
@Entity
@Table(name = "todo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Todo implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Lob
	private String title;

	private boolean done;
}
