package com.ruoogle.base.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Environment;
import android.os.Parcel;

/**
 * 文件操作相关
 * @author wei.chen
 *
 */
public final class UtilFile
{
	/**
	 * 删除指定目录下的�?��文件和文件夹(包括该目�?
	 * @param aRootFile
	 * 要删除的路径
	 */
	public static void deletePath(File aRootFile)
	{
		if(aRootFile == null || !aRootFile.isDirectory())
		{
			System.out.println("path is null or is not directory, do nothing");
			
			return ;
		}
		
		deletePath(aRootFile, true);
	}
	
	
	
	/**
	 * 删除指定目录下的�?��文件和文件夹(包括该目�?
	 * @param aRootPath
	 * 要删除的路径
	 */
	public static void deletePath(String aRootPath)
	{
		if(aRootPath == null || aRootPath.equals(""))
		{
			System.out.println("path is null, do nothing");
			
			return ;
		}
		
		deletePath(aRootPath, true);
		
	}
	
	
	
	/**
	 * 删除指定目录下的�?��文件和文件夹
	 * @param aRootPath
	 * 要删除的路径
	 * @param aIsIncludeRootPath
	 * 是否包括该目�?
	 */
	public static void deletePath(String aRootPath, boolean aIsIncludeRootPath)
	{
		if(aRootPath == null || aRootPath.equals(""))
		{
			System.out.println("path is null, do nothing");
			
			return ;
		}
		
		deletePath(new File(aRootPath), aIsIncludeRootPath);
	}
	
	
	
	/**
	 * 删除指定目录下的�?��文件和文件夹(包括该目�?
	 * @param aRootFile
	 * 要删除的路径
	 * @param aIsIncludeRootPath
	 * 是否包括该目�?
	 */
	public static void deletePath(File aRootFile, boolean aIsIncludeRootPath)
	{
		if(aRootFile == null || !aRootFile.isDirectory())
		{
			System.out.println("path is null or is not directory, do nothing");
			
			return ;
		}
		
		ArrayList<File> rootFileList = new ArrayList<File>();
		
		
		if(aIsIncludeRootPath)
		{
			rootFileList.add(aRootFile);	
		}
		
		findAll(aRootFile, rootFileList);
		
		deleteAll(rootFileList);
		
	}
	
	
	private static void findAll(File aTempRoot, List<File> aRootFileList)
	{
		File[] tmpFileList = aTempRoot.listFiles();
		
		if(tmpFileList != null)
		{
			List<File> fileList = Arrays.asList(tmpFileList);
			
			aRootFileList.addAll(fileList);
			
			for(File file : fileList)
			{
				findAll(file, aRootFileList);
			}
		}
	}
	
	
	private static void deleteAll(List<File> aRootFileList)
	{
		if(aRootFileList != null)
		{
			for(int i = aRootFileList.size() - 1; i >= 0; i--)
			{
				File file = aRootFileList.remove(i);
				
				if(!file.delete())
				{
					System.err.println("fail delete file");
				}
			}
		}
	}
	


	/**
	 * 搜索指定目录下的 文件(指定的文件类�?<br/>
	 * @param aFileList
	 * 搜索结果List
	 * @param aPath
	 * 搜索路径
	 * @param aSearchKey
	 * 搜索关键�?
	 * @param aFileFilter
	 * 过滤条件（扩展名数组，如�?txt, .html�?
	 * @param aIsIncludeChild
	 * 是否包含子文件夹
	 */
	public static void searchFileCurPath(List<File> aFileList, String aPath, 
			String aSearchKey, String[] aFileFilter, boolean aIsIncludeChild)
	{
		if(aFileList == null || aPath == null)
		{
			return;
		}
		
		File path = new File(aPath);
		
		if(path.exists() && path.isDirectory())
		{
			File[] fileList = path.listFiles();
			if(fileList != null)
			{
				for(File file : fileList)
				{
					if(aIsIncludeChild && file.isDirectory())
					{
						searchFileCurPath(aFileList, file.getAbsolutePath(), aSearchKey, aFileFilter, true);
					}
					else if(isTheFile(file, aFileFilter))
					{
						String fileName = getFileNameNoExtName(file).toLowerCase();
								
						if(fileName.contains(aSearchKey.toLowerCase()))
						{
							aFileList.add(file);
						}
					}
				}
			}
		}
	}
	
	
	
	/**
	 * 获取某个文件夹下 �?��指定类型�?文件个数
	 * @param aPath
	 * 路径
	 * @param aFileFilter
	 * 过滤条件（扩展名数组，如�?txt, .html�?
	 * @param aIsIncludeChild
	 * 是否包含子文件夹
	 * @return
	 * 路径为空时返�?
	 */
	public static int getFileCountCurPath(String aPath, String[] aFileFilter, boolean aIsIncludeChild)
	{
		if(aPath == null || aPath.equals(""))
		{
			return 0;
		}
		
		int count = 0;
		
		File path = new File(aPath);
		
		if(path.exists() && path.isDirectory())
		{
			File[] fileList = path.listFiles();
			if(fileList != null)
			{
				for(File file : fileList)
				{
					if(file.isDirectory())
					{
						count += getFileCountCurPath(file.getAbsolutePath(), aFileFilter, aIsIncludeChild);
					}
					else if(isTheFile(file, aFileFilter))
					{
						count++;
					}
				}
			}
		}
		
		return count;
	}
	
	
	/**
	 * 获取当前指定目录下的 文件�?�?符合条件的文�?(不包括子文件�?
	 * @param aFileList
	 * 返回的文件夹和文件列�?
	 * @param aPath
	 * 文件夹路�?
	 * @param aFileFilter
	 * 过滤条件（扩展名数组，如�?txt,.html�?
	 */
	public static void getFileCurPath(List<File> aFileList, String aPath, String[] aFileFilter)
	{
		if(aFileList == null || aPath == null)
		{
			return;
		}
		
		File path = new File(aPath);
		
		if(path.exists() && path.isDirectory())
		{
			File[] fileList = path.listFiles();
			
			if(fileList != null)
			{
				for(File file : fileList)
				{
					if(file.isDirectory() || isTheFile(file, aFileFilter))
					{
						aFileList.add(file);
					}
				}
			}
		}
	}
	
	
	/**
	 * 获取指定路径�?符合条件的文�?
	 * @param aFileList
	 * 返回的文件列�?
	 * @param aPath
	 * 文件夹路�?
	 * @param aFileFilter
	 * 过滤条件（扩展名数组，如�?txt,.html�?
	 * @param aIsIncludeChild
	 * 是否包含子文件夹
	 */
	public static void getFile(List<File> aFileList, String aPath, String[] aFileFilter, boolean aIsIncludeChild)
	{
		if(aFileList == null || aPath == null)
		{
			return ;
		}
		
		File path = new File(aPath);
		
		if(path.exists() && path.isDirectory())
		{
			File[] fileList = path.listFiles();
			
			if(fileList != null)
			{
				for(File file : fileList)
				{
					if(aIsIncludeChild && file.isDirectory())
					{
						getFile(aFileList, file.getAbsolutePath(), aFileFilter, aIsIncludeChild);
					}
					else if(isTheFile(file, aFileFilter))
					{
						aFileList.add(file);
					}
				}
			}
		}
	}
	
	
	/**
	 * 获取指定路径�?符合条件的文�?
	 * @param aFileList
	 * 返回的文件列�?
	 * @param aPath
	 * 文件夹路�?
	 * @param aFileFilter
	 * 过滤条件（扩展名数组，如�?txt,.html�?
	 * @param aIsIncludeChild
	 * 是否包含子文件夹
	 * @param aMaxCount
	 * �?��加载个数
	 */
	public static void getFile(List<File> aFileList, String aPath, String[] aFileFilter, boolean aIsIncludeChild, int aMaxCount)
	{
		if(aFileList == null || aPath == null)
		{
			return ;
		}
		
		File path = new File(aPath);
		
		if(path.exists() && path.isDirectory())
		{
			File[] fileList = path.listFiles();
			
			if(fileList != null)
			{
				for(File file : fileList)
				{
					if(aIsIncludeChild && file.isDirectory())
					{
						getFile(aFileList, file.getAbsolutePath(), aFileFilter, aIsIncludeChild, aMaxCount);
					}
					else if(isTheFile(file, aFileFilter))
					{
						aFileList.add(file);
						if(aFileList.size() >= aMaxCount)
						{
							break;
						}
					}
				}
			}
		}
	}
	
	
	/**
	 * 	判断文件是否符合过滤条件（文件扩展名�?
	 * @param File aFile
	 * 文件
	 * @param aFileFilter
	 * 过滤条件
	 * */
	public static boolean isTheFile(File aFile, String[] aFileFilter)
	{
		if(aFile == null || aFileFilter == null)
		{
			return false;
		}
		
		if(aFile.exists() && !aFile.isDirectory())
		{
			String fileName = aFile.getName().toLowerCase();
			
			int index = fileName.lastIndexOf(".");
			if(index == -1)
			{
				return false;
			}
			
			String onlyName = fileName.substring(0, index);
			if(onlyName == null || onlyName.equals(""))
			{
				return false;
			}
			
			for(String fileFilter : aFileFilter)
			{
				if(fileName.endsWith(fileFilter.toLowerCase()))
				{
					return true;
				}
			}
			
		}
		
		return false;
	}
	
	
	

	/**
	 * 获取文件类型(扩展�?
	 * @param aFile
	 * 文件
	 * @param aIsHasDot
	 * 是否包含'.'
	 * @return
	 * aIsHasDot为true:包含'.', �?txt<br/>
	 * aIsHasDot为false:不包�?.', 如txt<br/>
	 * 参数为null时，返回null<br/>
	 * 文件为目录时，返�?"
	 */
	public static String getFileType(File aFile, boolean aIsHasDot)
	{
		if(aFile == null)
		{
			return null;
		}
		
		if(aFile.isDirectory())
		{
			return "";
		}
		
		
		int index = aFile.getName().lastIndexOf(".");
		
		if(aIsHasDot)
		{
			return index != -1 ?
					aFile.getName().substring(index).toLowerCase()
					: "";
		}
		else
		{
			return index != -1 ?
					aFile.getName().substring(index + 1).toLowerCase()
					: "";
		}
		
	}
	
	
	/**
	 * 获取文件类型(扩展�?
	 * @param aFilePath
	 * 文件绝对路径
	 * @param aIsHasDot
	 * 是否包含'.'
	 * @return
	 * aIsHasDot为true:包含'.', �?txt<br/>
	 * aIsHasDot为false:不包�?.', 如txt<br/>
	 * 参数为null时，返回null<br/>
	 * 文件为目录时，返�?"
	 */
	public static String getFileType(String aFilePath, boolean aIsHasDot)
	{
		if(aFilePath == null)
		{
			return null;
		}
		
		File aFile = new File(aFilePath);
		
		if(aFile.isDirectory())
		{
			return "";
		}
		
		
		int index = aFile.getName().lastIndexOf(".");
		
		if(aIsHasDot)
		{
			return index != -1 ?
					aFile.getName().substring(index).toLowerCase()
					: "";
		}
		else
		{
			return index != -1 ?
					aFile.getName().substring(index + 1).toLowerCase()
					: "";
		}
		
	}

	/**
	 * 获取文件名称(仅名称，不包含路径和扩展�?
	 * @return
	 * 参数为null时，返回null
	 */
	public static String getFileNameNoExtName(File aFile)
	{
		if(aFile == null)
		{
			return null;
		}
		
		if(aFile.isDirectory())
		{
			return aFile.getName();
		}
		
		int index = aFile.getName().lastIndexOf(".");
		
		return index != -1 
				? aFile.getName().substring(0, index)
				: aFile.getName();
	}

	
	/**
	 * 复制文件
	 * @param sourceFile
	 * 原始文件
	 * @param destFile
	 * 复制后的文件
	 */
	public static void copyFile(File sourceFile, File destFile)
	{
		if(sourceFile == null || destFile == null)
		{
			return ;
		}
		
		try 
		{
			if(sourceFile.exists())
			{
				if(!destFile.exists())
				{
					if(!destFile.isDirectory())
					{
						File file = new File(destFile.getParent());
						if(!file.exists())
						{
							System.out.println("folder not exists, creat it");
							
							file.mkdirs();
						}
					}
					
					copyFile(new FileInputStream(sourceFile), new FileOutputStream(destFile));
				}
			}			
		
		}
		catch (IOException e) 
		{
			System.out.println("!!! ERROR IO EXCEPTION !!!");
		}
		
	}
	
	
	
	/**
	 * 复制文件
	 * @param sourceFile
	 * 原始文件
	 * @param destFile
	 * 复制后的文件
	 */
	public static void copyFile(String sourceFile, String destFile)
	{
		if(sourceFile == null || destFile == null)
		{
			return ;
		}
		
		copyFile(new File(sourceFile), new File(destFile));		
	}

	/**
	 * 复制文件
	 * @param sourceFile
	 * 原始文件
	 * @param destFile
	 * 复制后的文件
	 */
	public static void copyFile(InputStream sourceFileInputStream, OutputStream destFileOutputStream)
	{
		if(sourceFileInputStream == null || destFileOutputStream == null)
		{
			return ;
		}
		
		try 
		{
			
			byte buffer[] = new byte[2024];
			int len;
			while((len = sourceFileInputStream.read(buffer,0,2024)) != -1)
			{
				destFileOutputStream.write(buffer,0,len);
			}
			sourceFileInputStream.close();
			destFileOutputStream.close();			
		
		}
		catch (IOException e) 
		{
			System.out.println("!!! ERROR IO EXCEPTION !!!");
		}
	}
	
    /**
     * Get the external app cache directory.
     *
     * @param context The context to use
     * @return The external cache dir
     */
    @TargetApi(8)
    public static File getExternalCacheDir(Context context) {
        if (UtilTargetApi.hasFroyo()) {
            return context.getExternalCacheDir();
        }

        // Before Froyo we need to construct the external cache dir ourselves
        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }
	
	
    /**
     * Get a usable cache directory (external if available, internal otherwise).
     *
     * @param context The context to use
     * @param uniqueName A unique directory name to append to the cache dir
     * @return The cache dir
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {
        // Check if media is mounted or storage is built-in, if so, try and use external cache dir
        // otherwise use internal cache dir
        final String cachePath =
                Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                        !isExternalStorageRemovable() ? getExternalCacheDir(context).getPath() :
                                context.getCacheDir().getPath();

        return new File(cachePath + File.separator + uniqueName);
    }
    
    /**
     * Check if external storage is built-in or removable.
     *
     * @return True if external storage is removable (like an SD card), false
     *         otherwise.
     */
    @TargetApi(9)
    public static boolean isExternalStorageRemovable() {
        if (UtilTargetApi.hasGingerbread()) {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }
}
