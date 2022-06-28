package visualization;

public class Points {
    public final double[][] coordinate;
    public final double[][] velocity;
    public final int[] type;

    public final int quantity;
    public final int dimension;

    public Points(int quantity, int dimension) {
        this.quantity = quantity;
        this.dimension = dimension;
        this.coordinate = new double[quantity][dimension];
        this.velocity = new double[quantity][dimension];
        this.type = new int[quantity];
    }

    public void move(double dt) {
        for (int i = 0, j; i < quantity; ++i) {
            for (j = 0; j < dimension; ++j) {
                coordinate[i][j] += velocity[i][j] * dt / 10.0;
            }
        }
    }
}
