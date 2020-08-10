package tcgenplugin_2.handlers;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.core.runtime.*;
import org.eclipse.jface.operation.*;
import java.lang.reflect.InvocationTargetException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import java.io.*;
import org.eclipse.ui.*;
import org.eclipse.ui.ide.IDE;

/**
 * This is a sample new wizard. Its role is to create a new file 
 * resource in the provided container. If the container resource
 * (a folder or a project) is selected in the workspace 
 * when the wizard is opened, it will accept it as the target
 * container. The wizard creates one file with the extension
 * "mpe". If a sample multi-page editor (also available
 * as a template) is registered for the same extension, it will
 * be able to open it.
 */

public class NewSpecFileWizard extends Wizard implements INewWizard {
	private NewSpecFileWizardPage page;
	private ISelection selection;

	/**
	 * Constructor for SampleNewWizard.
	 */
	public NewSpecFileWizard() {
		super();
		setNeedsProgressMonitor(true);
	}
	
	/**
	 * Adding the page to the wizard.
	 */
	@Override
	public void addPages() {
		page = new NewSpecFileWizardPage(selection);
		addPage(page);
	}

	/**
	 * This method is called when 'Finish' button is pressed in
	 * the wizard. We will create an operation and run it
	 * using wizard as execution context.
	 */
	@Override
	public boolean performFinish() {
		final String containerName = page.getContainerName();
		final String className = page.getClassName();
		IRunnableWithProgress op = monitor -> {
			try {
				doFinish(containerName, className, monitor);
			} catch (CoreException e) {
				throw new InvocationTargetException(e);
			} finally {
				monitor.done();
			}
		};
		try {
			getContainer().run(true, false, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error", realException.getMessage());
			return false;
		}
		return true;
	}
	
	/**
	 * The worker method. It will find the container, create the
	 * file if missing or just replace its contents, and open
	 * the editor on the newly created file.
	 */

	private void doFinish(
		String containerName,
		String className,
		IProgressMonitor monitor)
		throws CoreException {
		// create a sample file
		monitor.beginTask("Creating " + className, 2);
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource resource = root.findMember(new Path(containerName));
		if (!resource.exists() || !(resource instanceof IContainer)) {
			throwCoreException("Container \"" + containerName + "\" does not exist.");
		}
		IContainer container = (IContainer) resource;
		final IFile file = container.getFile(new Path(className));
		IFile di = container.getFile(new Path(className+".di"));
		IFile no = container.getFile(new Path(className+".notation"));
		IFile uml = container.getFile(new Path(className+".uml"));
		IFile ocl = container.getFile(new Path(className+".ocl"));
		System.out.println(containerName+"\n"+className);
		try {
			InputStream ocltargetStream = getClass().getResourceAsStream("/SpecFiles/SpecFiles.ocl");
			ocl.create(ocltargetStream, false, null);
			InputStream ditargetStream = getClass().getResourceAsStream("/SpecFiles/SpecFiles.di");
			di.create(ditargetStream, false, null);
			InputStream umltargetStream = getClass().getResourceAsStream("/SpecFiles/SpecFiles.uml");
			uml.create(umltargetStream, false, null);
			InputStream notargetStream = getClass().getResourceAsStream("/SpecFiles/SpecFiles.notation");
			no.create(notargetStream, false, null);
//			InputStream stream = openContentStream();
//			if (file.exists()) {
//				file.setContents(stream, true, true, monitor);
//			} else {
//				file.create(stream, true, monitor);
//			}
//			stream.close();
		} catch (Exception e) {
		}
		//設定自動開啟Papyrus and OCL
		monitor.worked(1);
		monitor.setTaskName("Opening file for editing...");
		
		getShell().getDisplay().asyncExec(() -> {
			IWorkbenchPage page =
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			IWorkbenchPage page2 =
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			try {
				IDE.setDefaultEditor(ocl, "org.xtext.example.ocl.oCL.OCLPackage");
				IDE.openEditor(page, ocl, true);
				IDE.openEditor(page2, di, true);
			} catch (PartInitException e) {
			}
		});
		monitor.worked(1);
	}
	
	/**
	 * We will initialize file contents with a sample text.
	 */

	private InputStream openContentStream() {
		String contents =
			"This is the initial file contents for *.mpe file that should be word-sorted in the Preview page of the multi-page editor";
		return new ByteArrayInputStream(contents.getBytes());
	}

	private void throwCoreException(String message) throws CoreException {
		IStatus status =
			new Status(IStatus.ERROR, "NF", IStatus.OK, message, null);
		throw new CoreException(status);
	}

	/**
	 * We will accept the selection in the workbench to see if
	 * we can initialize from it.
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}
}