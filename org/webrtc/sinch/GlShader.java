package org.webrtc.sinch;

import android.opengl.GLES20;
import java.nio.FloatBuffer;

public class GlShader {
    private static final String TAG = "GlShader";
    private int program = GLES20.glCreateProgram();

    public GlShader(String str, String str2) {
        int compileShader = compileShader(35633, str);
        int compileShader2 = compileShader(35632, str2);
        if (this.program == 0) {
            throw new RuntimeException("glCreateProgram() failed. GLES20 error: " + GLES20.glGetError());
        }
        GLES20.glAttachShader(this.program, compileShader);
        GLES20.glAttachShader(this.program, compileShader2);
        GLES20.glLinkProgram(this.program);
        int[] iArr = new int[]{0};
        GLES20.glGetProgramiv(this.program, 35714, iArr, 0);
        if (iArr[0] != 1) {
            Logging.m777e(TAG, "Could not link program: " + GLES20.glGetProgramInfoLog(this.program));
            throw new RuntimeException(GLES20.glGetProgramInfoLog(this.program));
        }
        GLES20.glDeleteShader(compileShader);
        GLES20.glDeleteShader(compileShader2);
        GlUtil.checkNoGLES2Error("Creating GlShader");
    }

    private static int compileShader(int i, String str) {
        int glCreateShader = GLES20.glCreateShader(i);
        if (glCreateShader == 0) {
            throw new RuntimeException("glCreateShader() failed. GLES20 error: " + GLES20.glGetError());
        }
        GLES20.glShaderSource(glCreateShader, str);
        GLES20.glCompileShader(glCreateShader);
        int[] iArr = new int[]{0};
        GLES20.glGetShaderiv(glCreateShader, 35713, iArr, 0);
        if (iArr[0] != 1) {
            Logging.m777e(TAG, "Could not compile shader " + i + ":" + GLES20.glGetShaderInfoLog(glCreateShader));
            throw new RuntimeException(GLES20.glGetShaderInfoLog(glCreateShader));
        }
        GlUtil.checkNoGLES2Error("compileShader");
        return glCreateShader;
    }

    public int getAttribLocation(String str) {
        if (this.program == -1) {
            throw new RuntimeException("The program has been released");
        }
        int glGetAttribLocation = GLES20.glGetAttribLocation(this.program, str);
        if (glGetAttribLocation >= 0) {
            return glGetAttribLocation;
        }
        throw new RuntimeException("Could not locate '" + str + "' in program");
    }

    public int getUniformLocation(String str) {
        if (this.program == -1) {
            throw new RuntimeException("The program has been released");
        }
        int glGetUniformLocation = GLES20.glGetUniformLocation(this.program, str);
        if (glGetUniformLocation >= 0) {
            return glGetUniformLocation;
        }
        throw new RuntimeException("Could not locate uniform '" + str + "' in program");
    }

    public void release() {
        Logging.m776d(TAG, "Deleting shader.");
        if (this.program != -1) {
            GLES20.glDeleteProgram(this.program);
            this.program = -1;
        }
    }

    public void setVertexAttribArray(String str, int i, FloatBuffer floatBuffer) {
        if (this.program == -1) {
            throw new RuntimeException("The program has been released");
        }
        int attribLocation = getAttribLocation(str);
        GLES20.glEnableVertexAttribArray(attribLocation);
        GLES20.glVertexAttribPointer(attribLocation, i, 5126, false, 0, floatBuffer);
        GlUtil.checkNoGLES2Error("setVertexAttribArray");
    }

    public void useProgram() {
        if (this.program == -1) {
            throw new RuntimeException("The program has been released");
        }
        GLES20.glUseProgram(this.program);
        GlUtil.checkNoGLES2Error("glUseProgram");
    }
}
