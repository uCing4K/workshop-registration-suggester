% ==============================================================================
% UJI QUERY & PEMBUKTIAN BACKTRACKING / REKURSI
% File ini berisi 5 pengujian query utama sesuai spesifikasi LKM Pertemuan 12
% ==============================================================================

% Cara Menjalankan di SWI-Prolog CLI:
% 1. Buka terminal lalu ketik: swipl knowledge_base.pl
% 2. Jalankan query-query di bawah ini:

% ------------------------------------------------------------------------------
% Q1 - Cek Kelayakan (Single Proof / True)
% Pertanyaan: Apakah peserta p001 layak mengikuti workshop w01?
% Ekspektasi: true.
% Alasan: p001 aktif, w01 butuh basic_java, dan p001 sudah menguasai basic_java.
%
% ?- eligible(p001, w01).
% Output: true.

% ------------------------------------------------------------------------------
% Q2 - Cek Rekomendasi Gagal (False)
% Pertanyaan: Mengapa p003 tidak direkomendasikan ke w03?
% Ekspektasi: false (query gagal).
% Alasan: p003 aktif dan minat ai cocok dengan w03, namun seats_available(w03)
%         tidak ada di knowledge base (kursi penuh).
%
% ?- recommended(p003, w03).
% Output: false.

% ------------------------------------------------------------------------------
% Q3 - Rekomendasi Tunggal Berdasarkan Prasyarat Terbatas
% Pertanyaan: Workshop apa yang direkomendasikan untuk p002?
% Ekspektasi: W = w01.
% Alasan: p002 hanya punya skill basic_java (layak w01), belum punya oop (gagal w02).
%
% ?- recommended(p002, W).
% Output: W = w01.

% ------------------------------------------------------------------------------
% Q4 - Backtracking Multi-Solusi
% Pertanyaan: Workshop apa saja yang direkomendasikan untuk peserta p001?
% Ekspektasi: W = w01 ; W = w02 (Prolog menemukan alternatif saat ditekan ';').
% Alasan: p001 menguasai basic_java dan oop, minat backend, kursi w01 & w02 tersedia.
%
% ?- recommended(p001, W).
% Output:
% W = w01 ;
% W = w02.

% ------------------------------------------------------------------------------
% Q5 - Penelusuran Rekursif Rantai Prasyarat (Recursion & Transitivity)
% Pertanyaan: Skill apa saja yang harus dikuasai sebelum belajar design_pattern?
% Ekspektasi: X = advanced_java ; X = oop ; X = basic_java.
% Alasan: design_pattern -> advanced_java -> oop -> basic_java.
%
% ?- prereq_of(design_pattern, X).
% Output:
% X = advanced_java ;
% X = oop ;
% X = basic_java.
