package com.sinch.relinker.elf;

import com.sinch.relinker.elf.Elf.DynamicStructure;
import com.sinch.relinker.elf.Elf.Header;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Dynamic32Structure extends DynamicStructure {
    public Dynamic32Structure(ElfParser elfParser, Header header, long j, int i) {
        ByteBuffer allocate = ByteBuffer.allocate(4);
        allocate.order(header.bigEndian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
        long j2 = ((long) (i * 8)) + j;
        this.tag = elfParser.readWord(allocate, j2);
        this.val = elfParser.readWord(allocate, j2 + 4);
    }
}
