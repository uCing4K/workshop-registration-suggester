% ==============================================================================
% PROLOG KNOWLEDGE BASE: WORKSHOP REGISTRATION & SUGGESTER
% Tugas Besar Pemrograman Lanjut - TI-A Kelompok 5
% ==============================================================================

% ------------------------------------------------------------------------------
% 1. FAKTA (FACTS) - Minimal 10 Fakta
% ------------------------------------------------------------------------------

% active/1 : Peserta berstatus aktif dan berhak mendaftar
active(p001).
active(p002).
active(p003).

% completed/2 : Peserta sudah menguasai kompetensi/skill tertentu
completed(p001, basic_java).
completed(p001, oop).
completed(p002, basic_java).
completed(p003, basic_prolog).

% interest/2 : Minat dan fokus bidang peserta
interest(p001, backend).
interest(p002, backend).
interest(p003, ai).

% topic/2 : Topik atau bidang workshop
topic(w01, backend).
topic(w02, backend).
topic(w03, ai).

% requires/2 : Skill prasyarat langsung yang dibutuhkan untuk mengikuti workshop
requires(w01, basic_java).
requires(w02, oop).
requires(w03, basic_prolog).

% seats_available/1 : Workshop yang masih memiliki kuota/kursi tersedia
seats_available(w01).
seats_available(w02).
% Catatan: seats_available(w03) sengaja tidak ada karena kuota sudah habis/penuh.

% prerequisite/2 : Relasi rantai prasyarat antar-skill (directed prerequisite graph)
prerequisite(oop, basic_java).
prerequisite(advanced_java, oop).
prerequisite(design_pattern, advanced_java).

% ------------------------------------------------------------------------------
% 2. ATURAN PENALARAN (RULES) - R1 s.d R4
% ------------------------------------------------------------------------------

% R1 - eligible/2 : Peserta layak mengikuti workshop jika peserta aktif dan sudah
%                   menguasai skill prasyarat yang ditentukan workshop.
eligible(P, W) :-
    active(P),
    requires(W, S),
    completed(P, S).

% R2 - recommended/2 (Multi-kondisi) :
% Peserta direkomendasikan mengikuti workshop jika:
% 1. Peserta layak (eligible/2)
% 2. Minat peserta cocok dengan topik workshop
% 3. Workshop masih memiliki kursi yang tersedia (seats_available/1)
recommended(P, W) :-
    eligible(P, W),
    interest(P, T),
    topic(W, T),
    seats_available(W).

% R3 - prereq_of/2 (Aturan Rekursif dengan Base Case):
% Menelusuri seluruh rantai prasyarat langsung maupun tak langsung dari sebuah skill.
% Base Case: Ada relasi prasyarat langsung antara X dan Y.
prereq_of(X, Y) :-
    prerequisite(X, Y).

% Recursive Case: X butuh Y, dan Y butuh Z (secara transitif X butuh Z).
prereq_of(X, Z) :-
    prerequisite(X, Y),
    prereq_of(Y, Z).

% R4 - missing_skill/3 (Deteksi Kekurangan Prasyarat):
% Menentukan skill apa yang belum dikuasai peserta untuk workshop tertentu.
missing_skill(P, W, S) :-
    requires(W, S),
    \+ completed(P, S).
