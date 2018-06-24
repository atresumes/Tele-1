package com.sinch.relinker.elf;

import com.sinch.relinker.elf.Elf.Header;
import com.sinch.relinker.elf.Elf.SectionHeader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Section64Header extends SectionHeader {
    public Section64Header(ElfParser elfParser, Header header, int i) {
        ByteBuffer allocate = ByteBuffer.allocate(8);
        allocate.order(header.bigEndian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
        this.info = elfParser.readWord(allocate, (header.shoff + ((long) (header.shentsize * i))) + 44);
    }
}
