package com.nevergarden.myna.display;

import android.opengl.GLES20;

import com.nevergarden.myna.ds.Color;
import com.nevergarden.myna.gfx.GLProgram;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * A simple quad drawing a rectangular color.
 */
public class Quad extends DisplayObject implements Drawable {
    private static GLProgram program = null;
    protected final Color color;
    protected final float width;
    protected final float height;
    private final FloatBuffer vertexBuffer;
    /**
     * A matrix used by this model.
     */
    public float[] modelMatrix;

    /**
     * Default Constructor.
     */
    public Quad(Color color, float width, float height) {
        super();
        this.color = color;
        this.width = width;
        this.height = height;

        float[] quadVertices = new float[]{
                width, height, 0,
                0, height, 0,
                0, 0, 0,
                width, height, 0,
                0, 0, 0,
                width, 0, 0
        };

        if (program == null) {
            String vertexShader = "attribute vec3 aPosition;" +
                    "uniform mat4 uModel;" +
                    "void main() { gl_Position = uModel * vec4(aPosition,1.0); }";
            String fragmentShader = "precision mediump float; uniform vec4 uColor; void main() { gl_FragColor = uColor; }";
            program = GLProgram.createProgramFromSource(vertexShader, fragmentShader);
        }
        ByteBuffer bb = ByteBuffer.allocateDirect(quadVertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(quadVertices);
        vertexBuffer.position(0);
    }

    @Override
    public void recalculateMatrix() {
        super.recalculateMatrix();
        modelMatrix = new float[16];
        this.transform.get(this.modelMatrix);
    }

    @Override
    public void draw(int frame) {
        program.bind();
        int positionHandler = GLES20.glGetAttribLocation(program.nativeProgram, "aPosition");
        int modelHandler = GLES20.glGetUniformLocation(program.nativeProgram, "uModel");
        int colorHandler = GLES20.glGetUniformLocation(program.nativeProgram, "uColor");

        GLES20.glEnableVertexAttribArray(positionHandler);
        GLES20.glVertexAttribPointer(positionHandler, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);

        GLES20.glUniformMatrix4fv(modelHandler, 1, false, this.modelMatrix, 0);
        GLES20.glUniform4fv(colorHandler, 1, color.getColorRGBAV(), 0);

        // Draw 6 element of vertex array
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);

        // Clean up
        GLES20.glDisableVertexAttribArray(positionHandler);
    }
}