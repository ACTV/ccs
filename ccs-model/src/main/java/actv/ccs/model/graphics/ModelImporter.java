/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package actv.ccs.model.graphics;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *
 * @author Victor
 */
public class ModelImporter
{
  private ArrayList<Float> verts = new ArrayList<Float>();
  private ArrayList<Float> tris = new ArrayList<Float>(); 
  private ArrayList<Float> tcs = new ArrayList<Float>();
  private ArrayList<Float> UVs = new ArrayList<Float>();
  private ArrayList<Float> normals = new ArrayList<Float>();
  private ArrayList<Float> normVals = new ArrayList<Float>();

  public void parseOBJ(String filename) throws IOException
  {
    FileInputStream fs = new FileInputStream(filename);
      
    BufferedReader br = new BufferedReader(new InputStreamReader(fs));
    String line;

    while ((line = br.readLine()) != null)
    { if(line.startsWith("v "))
      { for(String s : line.split(" "))
        { if(s.matches("^[^a-z]+$") && !s.matches("\\s*"))
          { verts.add(Float.valueOf(s));
      } } }
      else if(line.startsWith("vt"))
      { for(String s : line.split(" "))
        { if(s.matches("^[^a-z]+$") && !s.matches("\\s*"))
          { UVs.add(Float.valueOf(s));
      } } }
      else if(line.startsWith("vn"))
      { for(String s : line.split(" "))
        { if(s.matches("^[^a-z]+$") && !s.matches("\\s*"))
          { normVals.add(Float.valueOf(s));
      } } }
      else if(line.startsWith("f"))
      {	for(String s : line.split(" "))
        { if(s.matches("^[^a-z]+$") && !s.matches("\\s*"))
          { String v = s.split("/")[0];
            String vt = s.split("/")[1];
            String vn = s.split("/")[2];

            int vertNum = (Integer.valueOf(v)-1)*3;
			int tcNum   = (Integer.valueOf(vt));
			int normNum = (Integer.valueOf(vn)-1)*3;

			tris.add(verts.get(vertNum));
            tris.add(verts.get((vertNum)+1));
            tris.add(verts.get((vertNum)+2));
            tris.add(1f);
        	
            tcs.add(UVs.get((tcNum-1)*2));
            tcs.add(UVs.get((tcNum-1)*2+1));

			normals.add(normVals.get(normNum));
            normals.add(normVals.get((normNum)+1));
            normals.add(normVals.get((normNum)+2));
            normals.add(1f);
    } } } }
    br.close();
  }
	
  public float[] getVertices()
  {
    float[] f = new float[tris.size()];
    for(int i = 0; i < tris.size(); i++)
    { f[i] = tris.get(i);
    }
    return f;
  }
	
  public int getNumVertices() { return (tris.size()/4); }

  public ArrayList<Float> getTris() { return tris; }
  public ArrayList<Float> getTcs() { return tcs; }

  public float[] getTextureCoordinates()
  {
    float[] f = new float[(tcs.size())];
    for(int i = 0; i < tcs.size(); i++)
    { f[i] = tcs.get(i);
    }
    return f;
  }
	
  public float[] getNormals()
  {
    float[] f = new float[(normals.size())];
    for(int i = 0; i < normals.size(); i++)
    { f[i] = normals.get(i);
    }
    return f;
  }	
}
