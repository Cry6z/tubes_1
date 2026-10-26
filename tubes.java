import java.util.Scanner;
public class tubes {

    public static double hitungSkorAkhir(double logika, double praktik, double wawancara) {
        return (40.0 * logika + 35.0 * praktik + 25.0 * wawancara) / 100.0;
    }
    public static String tentukanKategori(double skorAkhir) {
        if (skorAkhir >= 85) {
            return "Prioritas";
        } else if (skorAkhir >= 70) {
            return "Layak";
        } else if (skorAkhir >= 55) {
            return "Cadangan";
        } else {
            return "Belum memenuhi";
        }
    }
    public static String tentukanStatus(double skorAkhir, double skorPraktik) {
        if (skorAkhir >= 70 && skorPraktik >= 60) {
            return "Diterima";
        } else {
            return "Tidak diterima";
        }
    }
    public static void tampilkanRekap(String[] nama, double[][] skor,
                                      double[] skorAkhir, String[] kategori, String[] status) {
        System.out.println("\nREKAP SELEKSI");
        System.out.printf("%-3s %-16s %8s %8s %10s %10s  %-15s %-15s%n",
                "No", "Nama", "Logika", "Praktik", "Wawancara", "Skor Akhir", "Kategori", "Status");

        int diterima = 0;
        for (int i = 0; i < nama.length; i++) {
            System.out.printf("%-3d %-16s %8.2f %8.2f %10.2f %10.2f  %-15s %-15s%n",
                    i + 1, nama[i], skor[i][0], skor[i][1], skor[i][2],
                    skorAkhir[i], kategori[i], status[i]);
            if (status[i].equals("Diterima")) {
                diterima++;
            }
        }
        System.out.println("\nJumlah peserta diterima       : " + diterima);
        System.out.println("Jumlah peserta tidak diterima : " + (nama.length - diterima));
    }
    private static int bacaJumlahPeserta(Scanner sc) {
        while (true) {
            System.out.print("Masukkan jumlah calon peserta (1-30): ");
            try {
                int jumlah = Integer.parseInt(sc.nextLine().trim());
                if (jumlah >= 1 && jumlah <= 30) {
                    return jumlah;
                }
            } catch (NumberFormatException e) {
            }
            System.out.println("Jumlah peserta harus bilangan bulat 1-30.");
        }
    }
    private static double bacaSkor(Scanner sc, String komponen) {
        while (true) {
            System.out.print("  Skor " + komponen + " (0-100): ");
            try {
                double skor = Double.parseDouble(sc.nextLine().trim().replace(',', '.'));
                if (skor >= 0 && skor <= 100) {
                    return skor;
                }
            } catch (NumberFormatException e) {
            }
            System.out.println("Skor tidak valid.");
        }
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Program Seleksi Peserta Pelatihan Digital");
        int n = bacaJumlahPeserta(sc);

        String[] nama = new String[n];
        double[][] skor = new double[n][3];
        double[] skorAkhir = new double[n];
        String[] kategori = new String[n];
        String[] status = new String[n];

        for (int i = 0; i < n; i++) {
            System.out.println("\nData peserta ke-" + (i + 1));

            do {
                System.out.print("  Nama: ");
                nama[i] = sc.nextLine().trim();
                if (nama[i].isEmpty()) {
                    System.out.println("Nama tidak boleh kosong.");
                }
            } while (nama[i].isEmpty());

            skor[i][0] = bacaSkor(sc, "logika");
            skor[i][1] = bacaSkor(sc, "praktik");
            skor[i][2] = bacaSkor(sc, "wawancara");

            skorAkhir[i] = hitungSkorAkhir(skor[i][0], skor[i][1], skor[i][2]);
            kategori[i] = tentukanKategori(skorAkhir[i]);
            status[i] = tentukanStatus(skorAkhir[i], skor[i][1]);
        }
        tampilkanRekap(nama, skor, skorAkhir, kategori, status);
        sc.close();
    }
}