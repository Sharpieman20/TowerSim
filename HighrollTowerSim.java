import java.lang.Math;


public class Run {

    public static final int MOB_CAP = 60;

    public static final double ENDERMAN_WEIGHT = 0.0244;

    public static int getNumEndermenInRoll() {

        int mobs = 0;

        int endermen = 0;

        while (mobs <= MOB_CAP) {

            double mob = Math.random();

            int numMobs = 0;

            while (numMobs < 4) {

                if (Math.random() < 0.5) {

                    numMobs++;
                } else {
                    break;
                }
            }

            if (mob <= ENDERMAN_WEIGHT) {

                endermen += numMobs;
            }

            mobs += numMobs;
        }

        return endermen;
    }

    public static void main(String[] args) {

        int num_enderman_loops = 10_000_000;

        int num_tower_loops = 1_000_000;

        // int num_inner = 4;
        // int threshold = 8;

        // double rate = 0.0196;

        int total = 0;

        int[] chances = new int[MOB_CAP+1];

        for(int i = 0; i < num_enderman_loops; i++) {

            int endermen = getNumEndermenInRoll();

            chances[endermen]++;
        }

        int NUM_PEARLS = 12;

        int MAX_TIME = 110;

        for (int i = 0; i < num_tower_loops; i++) {

            int time = 0;

            int pearls = 0;

            while (pearls < NUM_PEARLS && time < MAX_TIME) {

                double roll = Math.random();

                int endermen = -1;

                while (roll > 0.0) {

                    roll -= ((double)chances[++endermen])/num_enderman_loops;
                }

                time += 8;

                if (endermen > 1) {

                    time += endermen * 3;

                    time += 7;

                    for (int j = 0; j < endermen; j++) {

                        if (Math.random() < 0.5) {

                            pearls++;
                        }
                    }
                }
            }

            if (pearls >= NUM_PEARLS && time < MAX_TIME) {

                total++;
            }
        }

        System.out.println(((double)total)/num_tower_loops);
    }
}
