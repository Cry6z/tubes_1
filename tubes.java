import java.util.Locale;
import java.util.Scanner;

/**
 * Studi Kasus 01 - Seleksi Peserta Pelatihan Digital
 *
 * Kolom array skor: 0 = logika, 1 = praktik, 2 = wawancara.
 */
public class tubes {

    // ===== Konstanta (menghindari "angka ajaib") =====
    static final int JUMLAH_PESERTA_MIN = 1;
    static final int JUMLAH_PESERTA_MAKS = 30;
    static final double SKOR_MIN = 0;
    static final double SKOR_MAKS = 100;

    static final int KOLOM_LOGIKA = 0;
    static final int KOLOM_PRAKTIK = 1;
    static final int KOLOM_WAWANCARA = 2;
    static final int JUMLAH_KOLOM_SKOR = 3;

    static final double BATAS_PRIORITAS = 85;
    static final double BATAS_LAYAK = 70;
    static final double BATAS_CADANGAN = 55;

    static final double BATAS_SKOR_AKHIR_DITERIMA = 70;
    static final double BATAS_SKOR_PRAKTIK_DITERIMA = 60;

    static final Locale LOKAL_INDONESIA = new Locale("id", "ID");

    // ===== Method input =====

    /** Membaca satu bilangan (desimal boleh, koma/titik diterima). Huruf ditolak dan diminta ulang. */
    static double bacaAngka(Scanner pemindai, String pesan) {
        while (true) {
            System.out.print(pesan);
            String masukan = pemindai.nextLine().trim().replace(',', '.');
            try {
                return Double.parseDouble(masukan);
            } catch (NumberFormatException e) {
                System.out.println("Masukan harus berupa angka.");
            }
        }
    }

    static int bacaJumlahPeserta(Scanner pemindai) {
        while (true) {
            double masukan = bacaAngka(pemindai,
                    "Jumlah calon peserta (" + JUMLAH_PESERTA_MIN + "-" + JUMLAH_PESERTA_MAKS + "): ");
            boolean bulat = masukan == Math.floor(masukan);
            if (bulat && masukan >= JUMLAH_PESERTA_MIN && masukan <= JUMLAH_PESERTA_MAKS) {
                return (int) masukan;
            }
            System.out.println("Jumlah peserta tidak valid. Masukkan bilangan bulat "
                    + JUMLAH_PESERTA_MIN + "-" + JUMLAH_PESERTA_MAKS + ".");
        }
    }

    static double bacaSkor(Scanner pemindai, String namaSkor) {
        while (true) {
            double skor = bacaAngka(pemindai, "  Skor " + namaSkor + " (0-100): ");
            if (skor >= SKOR_MIN && skor <= SKOR_MAKS) {
                return skor;
            }
            System.out.println("Skor tidak valid.");
        }
    }

    // ===== Method wajib =====

    /** Skor akhir = 0,40 x logika + 0,35 x praktik + 0,25 x wawancara (tanpa pembulatan). */
    static double hitungSkorAkhir(double logika, double praktik, double wawancara) {
        // Dikalikan 100 lalu dibagi 100 agar tidak muncul galat floating point
        // pada batas (mis. skor tepat 70 tidak menjadi 69,99999...).
        return (40 * logika + 35 * praktik + 25 * wawancara) / 100.0;
    }

    static String tentukanKategori(double skorAkhir) {
        if (skorAkhir >= BATAS_PRIORITAS) {
            return "Prioritas";
        } else if (skorAkhir >= BATAS_LAYAK) {
            return "Layak";
        } else if (skorAkhir >= BATAS_CADANGAN) {
            return "Cadangan";
        } else {
            return "Belum memenuhi";
        }
    }

    static String tentukanStatus(double skorAkhir, double skorPraktik) {
        if (skorAkhir >= BATAS_SKOR_AKHIR_DITERIMA && skorPraktik >= BATAS_SKOR_PRAKTIK_DITERIMA) {
            return "Diterima";
        }
        return "Tidak diterima";
    }

    static void tampilkanRekap(String[] namaPeserta, double[][] skorPeserta,
                               double[] skorAkhir, String[] kategori, String[] status) {
        String formatBaris = "%-3s %-15s %8s %8s %10s %11s %-15s %-15s%n";
        System.out.println();
        System.out.println("REKAP SELEKSI");
        System.out.printf(formatBaris, "No", "Nama", "Logika", "Praktik", "Wawancara",
                "Skor Akhir", "Kategori", "Status");
        System.out.println("-".repeat(97));

        int jumlahDiterima = 0;
        int jumlahTidakDiterima = 0;

        for (int i = 0; i < namaPeserta.length; i++) {
            System.out.printf(LOKAL_INDONESIA, formatBaris,
                    String.valueOf(i + 1),
                    namaPeserta[i],
                    String.format(LOKAL_INDONESIA, "%.2f", skorPeserta[i][KOLOM_LOGIKA]),
                    String.format(LOKAL_INDONESIA, "%.2f", skorPeserta[i][KOLOM_PRAKTIK]),
                    String.format(LOKAL_INDONESIA, "%.2f", skorPeserta[i][KOLOM_WAWANCARA]),
                    String.format(LOKAL_INDONESIA, "%.2f", skorAkhir[i]),
                    kategori[i],
                    status[i]);

            if (status[i].equals("Diterima")) {
                jumlahDiterima++;
            } else {
                jumlahTidakDiterima++;
            }
        }

        System.out.println("-".repeat(97));
        System.out.println("Jumlah peserta diterima       : " + jumlahDiterima);
        System.out.println("Jumlah peserta tidak diterima : " + jumlahTidakDiterima);
    }

    // ===== Program utama =====

    public static void main(String[] args) {
        Scanner pemindai = new Scanner(System.in);

        System.out.println("SELEKSI PESERTA PELATIHAN DIGITAL");
        int jumlahPeserta = bacaJumlahPeserta(pemindai);

        String[] namaPeserta = new String[jumlahPeserta];
        double[][] skorPeserta = new double[jumlahPeserta][JUMLAH_KOLOM_SKOR];
        double[] skorAkhir = new double[jumlahPeserta];
        String[] kategori = new String[jumlahPeserta];
        String[] status = new String[jumlahPeserta];

        for (int i = 0; i < jumlahPeserta; i++) {
            System.out.println("\nPeserta ke-" + (i + 1));

            String nama;
            do {
                System.out.print("  Nama: ");
                nama = pemindai.nextLine().trim();
                if (nama.isEmpty()) {
                    System.out.println("Nama tidak boleh kosong.");
                }
            } while (nama.isEmpty());
            namaPeserta[i] = nama;

            skorPeserta[i][KOLOM_LOGIKA] = bacaSkor(pemindai, "logika");
            skorPeserta[i][KOLOM_PRAKTIK] = bacaSkor(pemindai, "praktik");
            skorPeserta[i][KOLOM_WAWANCARA] = bacaSkor(pemindai, "wawancara");

            skorAkhir[i] = hitungSkorAkhir(
                    skorPeserta[i][KOLOM_LOGIKA],
                    skorPeserta[i][KOLOM_PRAKTIK],
                    skorPeserta[i][KOLOM_WAWANCARA]);
            kategori[i] = tentukanKategori(skorAkhir[i]);
            status[i] = tentukanStatus(skorAkhir[i], skorPeserta[i][KOLOM_PRAKTIK]);
        }

        tampilkanRekap(namaPeserta, skorPeserta, skorAkhir, kategori, status);
        pemindai.close();
    }
}