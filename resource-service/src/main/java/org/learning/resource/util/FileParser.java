package org.learning.resource.util;

import lombok.experimental.UtilityClass;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@UtilityClass
public class FileParser {

    public static Metadata getMp3Metadata(byte[] content) throws IOException, SAXException, TikaException {
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(content);
        ParseContext parseContext = new ParseContext();

        Mp3Parser Mp3Parser = new Mp3Parser();
        Mp3Parser.parse(inputStream, handler, metadata, parseContext);

        return metadata;
    }

}
