% =========================================================
% KNOWLEDGE BASE WORKSHOP REGISTRATION & SUGGESTER
% Auto-generated from Java RegistrationService runtime state
% =========================================================

% --- Fakta Status Peserta (active/1) ---
active(p001).
active(p003).
active(p002).

% --- Fakta Skill Peserta (completed/2) ---
completed(p004, basic_java).
completed(p001, oop).
completed(p001, basic_java).
completed(p003, basic_prolog).
completed(p002, basic_java).

% --- Fakta Minat Peserta (interest/2) ---
interest(p004, backend).
interest(p001, backend).
interest(p003, ai).
interest(p002, backend).

% --- Fakta Topik Workshop (topic/2) ---
topic(w02, backend).
topic(w01, backend).
topic(w03, ai).

% --- Fakta Prasyarat Workshop (requires/2) ---
requires(w02, oop).
requires(w01, basic_java).
requires(w03, basic_prolog).

% --- Fakta Ketersediaan Kursi (seats_available/1) ---
seats_available(w02).
seats_available(w01).
seats_available(w03).
