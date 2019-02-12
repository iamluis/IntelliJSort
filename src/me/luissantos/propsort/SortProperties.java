package me.luissantos.propsort;

import com.intellij.lang.Language;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.fileEditor.impl.LoadTextUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;

import java.io.IOException;
import java.util.*;

public class SortProperties extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {

        Project project = anActionEvent.getData(PlatformDataKeys.PROJECT);
        VirtualFile file = anActionEvent.getData(CommonDataKeys.PSI_FILE).getVirtualFile();
        Language lang = anActionEvent.getData(CommonDataKeys.PSI_FILE).getLanguage();
        if (!lang.is(Language.findLanguageByID("Properties"))) {
            Messages.showMessageDialog(project, "Cannot sort a non properties file.", "Information", Messages.getInformationIcon());
            return;
        }
        String string = LoadTextUtil.loadText(file).toString();
        List<String> lines = Arrays.asList(string.split("\n"));

        //Messages.showMessageDialog(project, lines.toString(), "Information", Messages.getInformationIcon());

        Map<String, List<String>> sortedLines = new HashMap<>();

        String title = "## MISC";
        for (String line : lines) {
            if (line.startsWith("## ")) {
                title = line;
            } else {
                sortedLines.putIfAbsent(title, new ArrayList<>());
                sortedLines.get(title).add(line);
            }
        }

        Set<String> keys = sortedLines.keySet();
        List<String> sortedKeys = new ArrayList<>();
        for (String key : keys) {
            sortedKeys.add(key);
        }

        sortedKeys.sort(String::compareTo);

        StringBuilder result= new StringBuilder();


        for (String s : sortedKeys) {
            result.append(s);
            result.append("\n");
            List<String> properties = sortedLines.get(s);
            properties.sort(String::compareTo);
            for (String property : properties) {
                result.append(property);
                result.append("\n");
            }

            result.append("\n\n");
        }

        try {
            file.setBinaryContent(result.toString().getBytes());
        } catch (IOException e) {
            Messages.showMessageDialog(project, "An error occurred while sorting.", "Properties Sort", Messages.getInformationIcon());
        }


        Messages.showMessageDialog(project, "Success!", "Properties Sort", Messages.getInformationIcon());


    }
}
