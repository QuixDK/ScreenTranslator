package ru.ssugt.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "recognized_voice")
@Getter
@Setter
@NoArgsConstructor
public class RecognizedVoice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column
    private String yandex_stt;

    @Column
    private byte[] wav_file_bytes;

    @Column
    private String correct_answer;
}
