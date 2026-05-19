package es.unican.is2.transportes;

/* Clase que representa un transporte realizado por un conductor */
public class Transporte {

	private double horas;
	private int ton;
	private int personas;
	private CategoriaTransporte cat;

	/**
	 * Constructor de la clase Transporte
	 * @param horas Horas que ha durado el transporte
	 * @param cat Categoria del transporte
	 * @param valor En caso de ser un transporte de tipo Personas,
	 * representa el numero de personas, en caso de ser de tipo Mercancias
	 * representa las toneladas
	 *
	 * CC: 1 (base) + 1 (if horas<=0 || valor<=0) + 1 (|| cat==null = condicion adicional)
	 *   + 1 (|| valor<=0 = condicion adicional) + 1 (if cat.equals Personas) = 5
	 * CCog: +1 (if) +1 (|| secuencia 1) +1 (|| secuencia 2) +1 (if nivel 0) = 4
	 */
	public Transporte(double horas, CategoriaTransporte cat, int valor) throws IllegalArgumentException {
		// CC: +1 if; +1 || primera secuencia; +1 || segunda secuencia
		if (horas <= 0 || valor <= 0 || cat == null) {
			throw new IllegalArgumentException();
		}
		this.horas = horas;
		this.cat = cat;
		// CC: +1 if
		if (cat.equals(CategoriaTransporte.Personas)) {
			this.personas = valor;
		} else {
			this.ton = valor;
		}
	}

	// CC: 1 (metodo secuencial) | CCog: 0
	public double horas() {
		return horas;
	}

	// CC: 1 (metodo secuencial) | CCog: 0
	public CategoriaTransporte categoria() {
		return cat;
	}

	// CC: 1 (metodo secuencial) | CCog: 0
	public int ton() {
		return ton;
	}

	// CC: 1 (metodo secuencial) | CCog: 0
	public int getPersonas() {
		return personas;
	}

}
