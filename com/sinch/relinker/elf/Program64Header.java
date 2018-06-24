package com.sinch.relinker.elf;

import com.sinch.relinker.elf.Elf.Header;
import com.sinch.relinker.elf.Elf.ProgramHeader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Program64Header extends ProgramHeader {
    public Program64Header(ElfParser elfParser, Header header, long j) {
        ByteBuffer allocate = ByteBuffer.allocate(8);
        allocate.order(header.bigEndian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
        long j2 = header.phoff + (((long) header.phentsize) * j);
        this.type = elfParser.readWord(allocate, j2);
        this.offset = elfParser.readLong(allocate, 8 + j2);
        this.vaddr = elfParser.readLong(allocate, 16 + j2);
        this.memsz = elfParser.readLong(allocate, j2 + 40);
    }
}
