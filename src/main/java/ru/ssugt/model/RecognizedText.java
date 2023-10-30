package ru.ssugt.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Table(name = "recognized_text")
@Getter
@Setter
@NoArgsConstructor
public class RecognizedText {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column
    private String yandex_ocr;

    @Column
    private String tesseract_ocr;

    @Column
    private String easy_ocr;

    @Column
    private byte[] base64_picture;

    @Column
    private String correct_answer;
}
