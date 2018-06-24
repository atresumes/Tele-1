package com.sinch.relinker.elf;

import android.support.v4.internal.view.SupportMenu;
import com.sinch.relinker.elf.Elf.DynamicStructure;
import com.sinch.relinker.elf.Elf.Header;
import com.sinch.relinker.elf.Elf.ProgramHeader;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ElfParser implements Elf, Closeable {
    private final int MAGIC = 1179403647;
    private final FileChannel channel;

    public ElfParser(File file) {
        if (file == null || !file.exists()) {
            throw new IllegalArgumentException("File is null or does not exist");
        }
        this.channel = new FileInputStream(file).getChannel();
    }

    private long offsetFromVma(Header header, long j, long j2) {
        for (long j3 = 0; j3 < j; j3++) {
            ProgramHeader programHeader = header.getProgramHeader(j3);
            if (programHeader.type == 1 && programHeader.vaddr <= j2 && j2 <= programHeader.vaddr + programHeader.memsz) {
                return (j2 - programHeader.vaddr) + programHeader.offset;
            }
        }
        throw new IllegalStateException("Could not map vma to file offset!");
    }

    public void close() {
        this.channel.close();
    }

    public Header parseHeader() {
        this.channel.position(0);
        ByteBuffer allocate = ByteBuffer.allocate(8);
        allocate.order(ByteOrder.LITTLE_ENDIAN);
        if (readWord(allocate, 0) != 1179403647) {
            throw new IllegalArgumentException("Invalid ELF Magic!");
        }
        short readByte = readByte(allocate, 4);
        boolean z = readByte(allocate, 5) == (short) 2;
        if (readByte == (short) 1) {
            return new Elf32Header(z, this);
        }
        if (readByte == (short) 2) {
            return new Elf64Header(z, this);
        }
        throw new IllegalStateException("Invalid class type!");
    }

    public List<String> parseNeededDependencies() {
        long j;
        this.channel.position(0);
        List<String> arrayList = new ArrayList();
        Header parseHeader = parseHeader();
        ByteBuffer allocate = ByteBuffer.allocate(8);
        allocate.order(parseHeader.bigEndian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
        long j2 = (long) parseHeader.phnum;
        if (j2 == 65535) {
            j2 = parseHeader.getSectionHeader(0).info;
        }
        for (j = 0; j < j2; j++) {
            ProgramHeader programHeader = parseHeader.getProgramHeader(j);
            if (programHeader.type == 2) {
                j = programHeader.offset;
                break;
            }
        }
        j = 0;
        if (j == 0) {
            return Collections.unmodifiableList(arrayList);
        }
        int i = 0;
        List<Long> arrayList2 = new ArrayList();
        long j3 = 0;
        DynamicStructure dynamicStructure;
        do {
            dynamicStructure = parseHeader.getDynamicStructure(j, i);
            if (dynamicStructure.tag == 1) {
                arrayList2.add(Long.valueOf(dynamicStructure.val));
            } else if (dynamicStructure.tag == 5) {
                j3 = dynamicStructure.val;
            }
            i++;
        } while (dynamicStructure.tag != 0);
        if (j3 == 0) {
            throw new IllegalStateException("String table offset not found!");
        }
        j2 = offsetFromVma(parseHeader, j2, j3);
        for (Long longValue : arrayList2) {
            arrayList.add(readString(allocate, longValue.longValue() + j2));
        }
        return arrayList;
    }

    protected void read(ByteBuffer byteBuffer, long j, int i) {
        byteBuffer.position(0);
        byteBuffer.limit(i);
        long j2 = 0;
        while (j2 < ((long) i)) {
            int read = this.channel.read(byteBuffer, j + j2);
            if (read == -1) {
                throw new EOFException();
            }
            j2 += (long) read;
        }
        byteBuffer.position(0);
    }

    protected short readByte(ByteBuffer byteBuffer, long j) {
        read(byteBuffer, j, 1);
        return (short) (byteBuffer.get() & 255);
    }

    protected int readHalf(ByteBuffer byteBuffer, long j) {
        read(byteBuffer, j, 2);
        return byteBuffer.getShort() & SupportMenu.USER_MASK;
    }

    protected long readLong(ByteBuffer byteBuffer, long j) {
        read(byteBuffer, j, 8);
        return byteBuffer.getLong();
    }

    protected String readString(ByteBuffer byteBuffer, long j) {
        StringBuilder stringBuilder = new StringBuilder();
        while (true) {
            long j2 = 1 + j;
            short readByte = readByte(byteBuffer, j);
            if (readByte == (short) 0) {
                return stringBuilder.toString();
            }
            stringBuilder.append((char) readByte);
            j = j2;
        }
    }

    protected long readWord(ByteBuffer byteBuffer, long j) {
        read(byteBuffer, j, 4);
        return ((long) byteBuffer.getInt()) & 4294967295L;
    }
}
