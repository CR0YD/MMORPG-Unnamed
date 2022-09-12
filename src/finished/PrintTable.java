package finished;

public class PrintTable {

	String[] header;
	String[][] body;

	int[] colLengths;

	public PrintTable(Object[] header, Object[][] body) throws Exception {
		if (body.length > 0 && header.length != body[0].length) {
			throw new Exception("Header needs to be same length as rows.");
		}

		this.header = new String[header.length];
		for (int i = 0; i < this.header.length; i++) {
			this.header[i] = String.valueOf(header[i]);
		}
		this.body = new String[body.length][body[0].length];
		for (int i = 0; i < this.body.length; i++) {
			for (int j = 0; j < this.body[i].length; j++) {
				this.body[i][j] = String.valueOf(body[i][j]);
			}
		}
		this.colLengths = this.findMaxColLengths(this.body);
		for (int i = 0; i < this.header.length; i++) {
			if (this.header[i].length() > this.colLengths[i]) {
				this.colLengths[i] = this.header[i].length();
			}
		}
	}

	public void print() {
		for (int i = 0; i < this.header.length; i++) {
			System.out.print(this.header[i]);
			for (int j = 0; j < this.colLengths[i] - this.header[i].length(); j++) {
				System.out.print(" ");
			}
			if (i < this.header.length - 1) {
				System.out.print("|");
			}
		}
		System.out.println();
		for (int i = 0; i < this.colLengths.length; i++) {
			for (int j = 0; j < this.colLengths[i]; j++) {
				System.out.print("-");
			}
			if (i < this.colLengths.length - 1) {
				System.out.print("+");
			}
		}
		System.out.println();
		for (int i = 0; i < this.body.length; i++) {
			for (int j = 0; j < this.body[i].length; j++) {
				System.out.print(this.body[i][j]);
				for (int k = 0; k < this.colLengths[j] - this.body[i][j].length(); k++) {
					System.out.print(" ");
				}
				if (j < this.body[i].length - 1) {
					System.out.print("|");
				}
			}
			System.out.println();
		}
	}

	private int[] findMaxColLengths(String[][] body) {
		int[] lengths = new int[body[0].length];

		for (int j = 0; j < body[0].length; j++) {
			int max = Integer.MIN_VALUE;
			for (int i = 0; i < body.length; i++) {
				if (body[i][j].length() > max) {
					max = body[i][j].length();
				}
			}
			lengths[j] = max;
		}

		return lengths;
	}
}
