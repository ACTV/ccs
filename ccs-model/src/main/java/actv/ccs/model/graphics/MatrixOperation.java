/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package actv.ccs.model.graphics;

import graphicslib3D.Matrix3D;

/**
 *
 * @author Victor
 */
public class MatrixOperation
{
    static public Matrix3D perspective(float fovy, float aspect, float n, float f)
    {
        float q = 1.0f / ((float) Math.tan(Math.toRadians(0.5f * fovy)));
        float A = q / aspect;
        float B = (n + f) / (n - f);
        float C = (2.0f * n * f) / (n - f);
        Matrix3D r = new Matrix3D();
        r.setElementAt(0, 0, A);
        r.setElementAt(0, 1, 0.0f);
        r.setElementAt(0, 2, 0.0f);
        r.setElementAt(0, 3, 0.0f);
        r.setElementAt(1, 0, 0.0f);
        r.setElementAt(1, 1, q);
        r.setElementAt(1, 2, 0.0f);
        r.setElementAt(1, 3, 0.0f);
        r.setElementAt(2, 0, 0.0f);
        r.setElementAt(2, 1, 0.0f);
        r.setElementAt(2, 2, B);
        r.setElementAt(2, 3, -1.0f);
        r.setElementAt(3, 0, 0.0f);
        r.setElementAt(3, 1, 0.0f);
        r.setElementAt(3, 2, C);
        r.setElementAt(3, 3, 0.0f);
        r = r.transpose();
        return r;
    }
    
    
    
}
