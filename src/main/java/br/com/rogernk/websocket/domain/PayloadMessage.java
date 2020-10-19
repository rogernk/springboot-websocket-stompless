package br.com.rogernk.websocket.domain;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class PayloadMessage implements Serializable {

	private static final long serialVersionUID = -4905290555442496846L;

	private String name;
}
