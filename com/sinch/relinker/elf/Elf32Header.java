package com.sinch.relinker.elf;

import com.sinch.relinker.elf.Elf.DynamicStructure;
import com.sinch.relinker.elf.Elf.Header;
import com.sinch.relinker.elf.Elf.ProgramHeader;
import com.sinch.relinker.elf.Elf.SectionHeader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Elf32Header extends Header {
    private final ElfParser parser;

    public Elf32Header(boolean z, ElfParser elfParser) {
        this.bigEndian = z;
        this.parser = elfParser;
        ByteBuffer allocate = ByteBuffer.allocate(4);
        allocate.order(z ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
        this.type = elfParser.readHalf(allocate, 16);
        this.phoff = elfParser.readWord(allocate, 28);
        this.shoff = elfParser.readWord(allocate, 32);
        this.phentsize = elfParser.readHalf(allocate, 42);
        this.phnum = elfParser.readHalf(allocate, 44);
        this.shentsize = elfParser.readHalf(allocate, 46);
        this.shnum = elfParser.readHalf(allocate, 48);
        this.shstrndx = elfParser.readHalf(allocate, 50);
    }

    public DynamicStructure getDynamicStructure(long j, int i) {
        return new Dynamic32Structure(this.parser, this, j, i);
    }

    public ProgramHeader getProgramHeader(long j) {
        return new Program32Header(this.parser, this, j);
    }

    public SectionHeader getSectionHeader(int i) {
        return new Section32Header(this.parser, this, i);
    }
}
