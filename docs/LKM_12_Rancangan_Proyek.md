# LEMBAR KERJA MAHASISWA (LKM)
## Pertemuan 12 - Project Design & Planning Tugas Besar

### Identitas Kelompok
* **Mata kuliah / Program**: Pemrograman Lanjut / S1 Teknik Informatika
* **Offering / Kelas**: TI-A
* **Kelompok / Tema**: Kelompok 5 / Workshop Registration & Suggester
* **Anggota (nama dan NIM)**:
  1. Aditya Widyatamaka Tri Utomo (250535623587)
  2. Chasbiah Azzahra (250535621731)
  3. Dzauq Bachrul ‘Ulum (250535621835)
  4. Elshifa Viorissa (250535621859)
* **Tanggal / Durasi**: 19 November 2026
* **Perangkat**: JDK 17+, VS Code/editor, SWI-Prolog, Word/diagram editor

---

### A. Capaian Pembelajaran
* Mengidentifikasi permasalahan dan menentukan batas ruang lingkup proyek yang dapat dikerjakan.
* Merancang model class Java menggunakan OOP, abstract class, interface, Collections/Generics, Stream API, dan exception handling.
* Merancang knowledge base Prolog (facts, rules, queries) yang mendukung penalaran, rekursi, dan backtracking yang bermakna.
* Menentukan keterkaitan data dan hasil antara modul Java dan modul Prolog.
* Menyusun rencana pengujian, dokumentasi, pembagian pekerjaan, dan serah terima untuk kelompok lain.
* Mempresentasikan serta mempertanggungjawabkan keputusan rancangan dalam bentuk dokumentasi / portofolio.

---

### B. Pengantar & Batas Teknis
* **Java**: Mengelola objek, alur aplikasi, penyimpanan sementara, validasi, dan pengolahan koleksi.
* **Prolog**: Memodelkan pengetahuan sebagai fakta dan rule, lalu mencari solusi melalui query.
* **Batas Teknis**: Java dan SWI-Prolog tidak diwajibkan untuk diintegrasikan secara runtime. Keduanya boleh dijalankan sebagai dua modul terpisah selama memakai domain, identitas data, dan aturan yang konsisten, di mana mahasiswa harus menunjukkan hubungan input dan hasilnya.

---

### C. Alur Penugasan M12 - M15
* **Minggu 12**: Analisis, UML, knowledge base, rencana uji -> Proposal/rancangan proyek
* **Minggu 13**: Coding Java + Prolog, testing, dokumentasi -> Project yang siap diuji pihak lain
* **Minggu 14**: Bertukar proyek, code review, refactoring, fitur baru -> Modifikasi proyek kelompok lain
* **Minggu 15**: Evaluasi perubahan, regresi, presentasi final -> Project final dan laporan

---

### D. Stage 1 - Problem Discovery & Pemilihan Tema
* **Judul Proyek**: Workshop Registration & Suggester: aplikasi pendaftaran dan rekomendasi workshop kampus
* **Calon Pengguna**: Mahasiswa peserta workshop, panitia/pengurus himpunan penyelenggara, dan dosen pembina kegiatan.
* **Permasalahan Utama**: Pendaftaran workshop masih dicatat manual di spreadsheet sehingga kapasitas sering terlampaui dan terjadi pendaftaran ganda. Mahasiswa juga kesulitan mengetahui workshop mana yang sesuai kemampuan dan minatnya. Panitia harus mengecek prasyarat peserta satu per satu.
* **Mengapa Pemrograman Diperlukan**: Pengecekan kapasitas, validasi, dan rekap harus otomatis dan konsisten. Penentuan kelayakan peserta juga membutuhkan aturan prasyarat berantai yang dapat ditelusuri alasannya.
* **Batasan Awal**: Tidak memakai database, tidak ada GUI/web, tidak ada pembayaran, tidak ada login/autentikasi, dan tidak ada notifikasi email. Data cukup disimpan di memori dan file teks.
* **Alasan Java**: Mengelola objek (peserta, workshop, pendaftaran), menyimpan data dalam Collection, memvalidasi kapasitas dan input, serta merekap data dengan Stream/Lambda. Pengelolaan alur menu dan penanganan error paling alami dilakukan dengan OOP dan exception handling.
* **Alasan Prolog**: Menalar kelayakan dan rekomendasi. Aturan "aktif + sudah menguasai prasyarat + minat sesuai + kursi tersedia" ditulis sebagai satu rule deklaratif, dan rantai prasyarat skill (A butuh B, B butuh C) ditelusuri dengan rekursi. Backtracking otomatis menghasilkan semua workshop yang cocok tanpa perlu menulis perulangan dan percabangan bersarang di Java.

---

### E. Stage 2 - Requirements & Scope Definition
#### Fitur Java:
* **J1**: Tambah data peserta dan workshop (Input: id, nama, status aktif, judul, topik, kapasitas, tanggal -> Objek tersimpan di Map/List; id sama ditolak).
* **J2**: Mendaftarkan peserta ke workshop (Input: idPeserta, idWorkshop -> Pendaftaran berhasil dan kursi berkurang; ditolak jika kapasitas penuh atau peserta sudah terdaftar).
* **J3**: Menampilkan workshop yang masih ada kursi, urut tanggal (Input: data workshop -> Daftar workshop yang belum penuh dari tanggal terdekat).
* **J4**: Rekap jumlah pendaftar per workshop (Input: data pendaftaran -> Tabel idWorkshop dan jumlah pendaftar).

#### Kebutuhan Prolog:
* **P1**: Apakah peserta p001 layak mengikuti workshop w01? (`eligible(p001, w01).` -> `true`)
* **P2**: Workshop apa saja yang direkomendasikan untuk peserta p001? (`recommended(p001, W).` -> `w01 ; w02`)
* **P3**: Mengapa p003 tidak direkomendasikan ke w03? (`recommended(p003, w03).` -> `false`)
* **P4 (rekursi/backtracking)**: Skill apa saja yang harus dikuasai sebelum Design Pattern? (`prereq_of(design_pattern, X).` -> `advanced_java ; oop ; basic_java`)

#### Business Rules & Batasan Kualitas:
* **Business Rule 1**: Kapasitas workshop bersifat maksimum. Jika jumlah pendaftar sudah sama dengan kapasitas, pendaftaran berikutnya ditolak.
* **Business Rule 2**: Satu peserta tidak boleh mendaftar dua kali pada workshop yang sama.
* **Business Rule 3**: Hanya peserta berstatus aktif yang boleh didaftarkan dan direkomendasikan.
* **Kualitas 1**: Input non-angka pada menu tidak boleh menghentikan program; tampilkan peringatan lalu ulangi menu.
* **Kualitas 2**: ID peserta/workshop yang tidak ditemukan menghasilkan pesan jelas, bukan crash (NullPointerException).

---

### F. Stage 3 - Perancangan Java
* **Class & Interfaces**:
  1. `Person` (abstract class)
  2. `Participant` (extends `Person`)
  3. `Workshop`
  4. `Registration`
  5. `Registrable` (interface)
  6. `RegistrationService` (implements `Registrable`)
  7. `CapacityFullException`
  8. `DuplicateRegistrationException`
  9. `Main`
* **Penerapan OOP**:
  * Encapsulation: `Workshop` membatasi modifikasi kapasitas dan pendaftar hanya via method.
  * Inheritance + Overriding: `Participant` mewarisi `Person` dan meng-override `getRole()`.
  * Polymorphism: Pemanggilan method `getRole()` melalui referensi bertipe `Person`.
  * Abstract class: `Person` sebagai cetak biru entitas tanpa instansiasi langsung.
  * Interface: `Registrable` memisahkan kontrak pendaftaran dari implementasinya.
* **Stream API**:
  1. Filter workshop belum penuh dan diurutkan berdasarkan tanggal (`LocalDate`).
  2. Rekapitulasi jumlah registrasi per workshop menggunakan `groupingBy` dan `counting`.

---

### G. Stage 4 - Perancangan Prolog Knowledge Base
* **Predikat Fakta**: `active/1`, `completed/2`, `interest/2`, `topic/2`, `requires/2`, `seats_available/1`, `prerequisite/2`.
* **Rules**:
  * `eligible(P, W) :- active(P), requires(W, S), completed(P, S).`
  * `recommended(P, W) :- eligible(P, W), interest(P, T), topic(W, T), seats_available(W).`
  * `prereq_of(X, Y) :- prerequisite(X, Y).`
  * `prereq_of(X, Z) :- prerequisite(X, Y), prereq_of(Y, Z).`
  * `missing_skill(P, W, S) :- requires(W, S), \+ completed(P, S).`

---

### H. Stage 5 - Keterkaitan Java dan Prolog
* ID Java (`P001`, `W01`) dikonversi ke atom Prolog (`p001`, `w01`).
* Skill String di Java dikonversi menjadi atom Prolog dengan penggantian spasi menjadi *underscore*.
* Java dapat mengekspor fakta dinamis (`active/1`, `completed/2`, `seats_available/1`) langsung ke file `.pl`.
