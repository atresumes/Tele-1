package com.sinch.relinker.elf;

import com.sinch.relinker.elf.Elf.DynamicStructure;
import com.sinch.relinker.elf.Elf.Header;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Dynamic64Structure extends DynamicStructure {
    public Dynamic64Structure(ElfParser elfParser, Header header, long j, int i) {
        ByteBuffer allocate = ByteBuffer.allocate(4);
        allocate.order(header.bigEndian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
        long j2 = ((long) (i * 16)) + j;
        this.tag = elfParser.readLong(allocate, j2);
        this.val = elfParser.readLong(allocate, j2 + 8);
    }
}
