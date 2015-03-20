/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package actv.ccs.model.graphics;

import graphicslib3D.Vertex3D;
import java.io.IOException;

/**
 *
 * @author Victor
 */
public class ImportedModel
{
  private static final long serialVersionUID = 1L;
  private Vertex3D [] vertices;
  private int numVertices;
  private int [] indices;
  private ModelImporter modelImporter;

  public ImportedModel(String filename) throws IOException
  { modelImporter = new ModelImporter();
  modelImporter.parseOBJ(filename);
      float[] verts   = modelImporter.getVertices();
      float[] normals = modelImporter.getNormals();
      float[] tcs     = modelImporter.getTextureCoordinates();
      numVertices     = modelImporter.getNumVertices();
      vertices = new Vertex3D[modelImporter.getNumVertices()];
      for(int i = 0; i < vertices.length; i++)
      { int j = i * 4;
      int k = i * 2;
      vertices[i] = new Vertex3D(verts[j], verts[j+1], verts[j+2]);
      vertices[i].setS(tcs[k]);
      vertices[i].setT(tcs[k+1]);
      vertices[i].setNormal(normals[j], normals[j+1], normals[j+2]);
      }
      indices = new int[modelImporter.getNumVertices()];
      for(int i = 0; i < indices.length; i++) { indices[i] = i; }
  }
  public Vertex3D[] getVertices() { return vertices; }	
  public int getNumVertices() { return numVertices; }

  public int[] getIndices() { return indices; }
  public int getNumIndices() { return indices.length; }
}
