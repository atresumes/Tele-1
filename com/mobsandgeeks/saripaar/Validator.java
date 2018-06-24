package com.mobsandgeeks.saripaar;

import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.mobsandgeeks.saripaar.annotation.Checked;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.IpAddress;
import com.mobsandgeeks.saripaar.annotation.NumberRule;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Regex;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.mobsandgeeks.saripaar.annotation.Select;
import com.mobsandgeeks.saripaar.annotation.TextRule;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Validator {
    static final boolean DEBUG = false;
    static final String TAG = "Validator";
    private boolean mAnnotationsProcessed;
    private AsyncTask<Void, Void, ViewRulePair> mAsyncValidationTask;
    private Object mController;
    private Map<String, Object> mProperties;
    private ValidationListener mValidationListener;
    private List<ViewRulePair> mViewsAndRules;

    class C03581 extends AsyncTask<Void, Void, ViewRulePair> {
        C03581() {
        }

        protected ViewRulePair doInBackground(Void... params) {
            return Validator.this.validateAllRules();
        }

        protected void onPostExecute(ViewRulePair pair) {
            if (pair == null) {
                Validator.this.mValidationListener.onValidationSucceeded();
            } else {
                Validator.this.mValidationListener.onValidationFailed(pair.view, pair.rule);
            }
            Validator.this.mAsyncValidationTask = null;
        }

        protected void onCancelled() {
            Validator.this.mAsyncValidationTask = null;
        }
    }

    private class AnnotationFieldPair {
        public Annotation annotation;
        public Field field;

        public AnnotationFieldPair(Annotation annotation, Field field) {
            this.annotation = annotation;
            this.field = field;
        }
    }

    private class AnnotationFieldPairCompartor implements Comparator<AnnotationFieldPair> {
        private AnnotationFieldPairCompartor() {
        }

        public int compare(AnnotationFieldPair lhs, AnnotationFieldPair rhs) {
            int lhsOrder = getAnnotationOrder(lhs.annotation);
            int rhsOrder = getAnnotationOrder(rhs.annotation);
            if (lhsOrder < rhsOrder) {
                return -1;
            }
            return lhsOrder == rhsOrder ? 0 : 1;
        }

        private int getAnnotationOrder(Annotation annotation) {
            Class<?> annotatedClass = annotation.annotationType();
            if (annotatedClass.equals(Checked.class)) {
                return ((Checked) annotation).order();
            }
            if (annotatedClass.equals(ConfirmPassword.class)) {
                return ((ConfirmPassword) annotation).order();
            }
            if (annotatedClass.equals(Email.class)) {
                return ((Email) annotation).order();
            }
            if (annotatedClass.equals(IpAddress.class)) {
                return ((IpAddress) annotation).order();
            }
            if (annotatedClass.equals(NumberRule.class)) {
                return ((NumberRule) annotation).order();
            }
            if (annotatedClass.equals(Password.class)) {
                return ((Password) annotation).order();
            }
            if (annotatedClass.equals(Regex.class)) {
                return ((Regex) annotation).order();
            }
            if (annotatedClass.equals(Required.class)) {
                return ((Required) annotation).order();
            }
            if (annotatedClass.equals(Select.class)) {
                return ((Select) annotation).order();
            }
            if (annotatedClass.equals(TextRule.class)) {
                return ((TextRule) annotation).order();
            }
            throw new IllegalArgumentException(String.format("%s is not a Saripaar annotation", new Object[]{annotatedClass.getName()}));
        }
    }

    public interface ValidationListener {
        void onValidationFailed(View view, Rule<?> rule);

        void onValidationSucceeded();
    }

    private class ViewRulePair {
        public Rule rule;
        public View view;

        public ViewRulePair(View view, Rule<?> rule) {
            this.view = view;
            this.rule = rule;
        }
    }

    private Validator() {
        this.mAnnotationsProcessed = false;
        this.mViewsAndRules = new ArrayList();
        this.mProperties = new HashMap();
    }

    public Validator(Object controller) {
        this();
        if (controller == null) {
            throw new IllegalArgumentException("'controller' cannot be null");
        }
        this.mController = controller;
    }

    public void put(View view, Rule<?> rule) {
        if (rule == null) {
            throw new IllegalArgumentException("'rule' cannot be null");
        }
        this.mViewsAndRules.add(new ViewRulePair(view, rule));
    }

    public void put(View view, List<Rule<?>> rules) {
        if (rules == null) {
            throw new IllegalArgumentException("'rules' cannot be null");
        }
        for (Rule rule : rules) {
            put(view, rule);
        }
    }

    public void put(Rule<?> rule) {
        put(null, (Rule) rule);
    }

    public synchronized void validate() {
        if (this.mValidationListener == null) {
            throw new IllegalStateException("Set a " + ValidationListener.class.getSimpleName() + " before attempting to validate.");
        }
        ViewRulePair failedViewRulePair = validateAllRules();
        if (failedViewRulePair == null) {
            this.mValidationListener.onValidationSucceeded();
        } else {
            this.mValidationListener.onValidationFailed(failedViewRulePair.view, failedViewRulePair.rule);
        }
    }

    public void validateAsync() {
        if (this.mValidationListener == null) {
            throw new IllegalStateException("Set a " + ValidationListener.class.getSimpleName() + " before attempting to validate.");
        }
        if (this.mAsyncValidationTask != null) {
            this.mAsyncValidationTask.cancel(true);
            this.mAsyncValidationTask = null;
        }
        this.mAsyncValidationTask = new C03581();
        this.mAsyncValidationTask.execute((Void[]) null);
    }

    public boolean isValidating() {
        return (this.mAsyncValidationTask == null || this.mAsyncValidationTask.getStatus() == Status.FINISHED) ? false : true;
    }

    public boolean cancelAsync() {
        if (this.mAsyncValidationTask == null) {
            return false;
        }
        boolean cancelled = this.mAsyncValidationTask.cancel(true);
        this.mAsyncValidationTask = null;
        return cancelled;
    }

    public ValidationListener getValidationListener() {
        return this.mValidationListener;
    }

    public void setValidationListener(ValidationListener validationListener) {
        this.mValidationListener = validationListener;
    }

    public void setProperty(String name, Object value) {
        if (name == null) {
            throw new IllegalArgumentException("'name' cannot be null");
        }
        this.mProperties.put(name, value);
    }

    public Object getProperty(String name) {
        if (name != null) {
            return this.mProperties.get(name);
        }
        throw new IllegalArgumentException("'name' cannot be null");
    }

    public Object removeProperty(String name) {
        return name != null ? this.mProperties.remove(name) : null;
    }

    public boolean containsProperty(String name) {
        return name != null ? this.mProperties.containsKey(name) : false;
    }

    public void removeAllProperties() {
        this.mProperties.clear();
    }

    public void removeRulesFor(View view) {
        if (view == null) {
            throw new IllegalArgumentException("'view' cannot be null");
        }
        int index = 0;
        while (index < this.mViewsAndRules.size()) {
            if (((ViewRulePair) this.mViewsAndRules.get(index)).view == view) {
                this.mViewsAndRules.remove(index);
            } else {
                index++;
            }
        }
    }

    private ViewRulePair validateAllRules() {
        if (!this.mAnnotationsProcessed) {
            createRulesFromAnnotations(getSaripaarAnnotatedFields());
            this.mAnnotationsProcessed = true;
        }
        if (this.mViewsAndRules.size() == 0) {
            Log.i(TAG, "No rules found. Passing validation by default.");
            return null;
        }
        for (ViewRulePair pair : this.mViewsAndRules) {
            if (pair != null && ((pair.view == null || (pair.view.isShown() && pair.view.isEnabled())) && !pair.rule.isValid(pair.view))) {
                return pair;
            }
        }
        return null;
    }

    private void createRulesFromAnnotations(List<AnnotationFieldPair> annotationFieldPairs) {
        TextView passwordTextView = null;
        TextView confirmPasswordTextView = null;
        for (AnnotationFieldPair pair : annotationFieldPairs) {
            ViewRulePair viewRulePair;
            if (pair.annotation.annotationType().equals(Password.class)) {
                if (passwordTextView == null) {
                    passwordTextView = (TextView) getView(pair.field);
                } else {
                    throw new IllegalStateException("You cannot annotate two fields in the same Activity with @Password.");
                }
            }
            if (pair.annotation.annotationType().equals(ConfirmPassword.class)) {
                if (passwordTextView == null) {
                    throw new IllegalStateException("A @Password annotated field is required before you can use @ConfirmPassword.");
                } else if (confirmPasswordTextView != null) {
                    throw new IllegalStateException("You cannot annotate two fields in the same Activity with @ConfirmPassword.");
                } else if (confirmPasswordTextView == null) {
                    confirmPasswordTextView = (TextView) getView(pair.field);
                }
            }
            if (pair.annotation.annotationType().equals(ConfirmPassword.class)) {
                viewRulePair = getViewAndRule(pair.field, pair.annotation, passwordTextView);
            } else {
                viewRulePair = getViewAndRule(pair.field, pair.annotation, new Object[0]);
            }
            if (viewRulePair != null) {
                this.mViewsAndRules.add(viewRulePair);
            }
        }
    }

    private ViewRulePair getViewAndRule(Field field, Annotation annotation, Object... params) {
        View view = getView(field);
        if (view == null) {
            Log.w(TAG, String.format("Your %s - %s is null. Please check your field assignment(s).", new Object[]{field.getType().getSimpleName(), field.getName()}));
            return null;
        }
        Rule<?> rule;
        if (params == null || params.length <= 0) {
            rule = AnnotationRuleFactory.getRule(field, view, annotation);
        } else {
            rule = AnnotationRuleFactory.getRule(field, view, annotation, params);
        }
        if (rule != null) {
            return new ViewRulePair(view, rule);
        }
        return null;
    }

    private View getView(Field field) {
        try {
            field.setAccessible(true);
            return (View) field.get(this.mController);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        }
        return null;
    }

    private List<AnnotationFieldPair> getSaripaarAnnotatedFields() {
        List<AnnotationFieldPair> annotationFieldPairs = new ArrayList();
        for (Field field : getViewFieldsWithAnnotations()) {
            for (Annotation annotation : field.getAnnotations()) {
                if (isSaripaarAnnotation(annotation)) {
                    annotationFieldPairs.add(new AnnotationFieldPair(annotation, field));
                }
            }
        }
        Collections.sort(annotationFieldPairs, new AnnotationFieldPairCompartor());
        return annotationFieldPairs;
    }

    private List<Field> getViewFieldsWithAnnotations() {
        List<Field> fieldsWithAnnotations = new ArrayList();
        for (Field field : getAllViewFields()) {
            Annotation[] annotations = field.getAnnotations();
            if (!(annotations == null || annotations.length == 0)) {
                fieldsWithAnnotations.add(field);
            }
        }
        return fieldsWithAnnotations;
    }

    private List<Field> getAllViewFields() {
        List<Field> viewFields = new ArrayList();
        Class<?> superClass = null;
        if (this.mController != null) {
            viewFields.addAll(getDeclaredViewFields(this.mController.getClass()));
            superClass = this.mController.getClass().getSuperclass();
        }
        while (superClass != null && !superClass.equals(Object.class)) {
            List<Field> declaredViewFields = getDeclaredViewFields(superClass);
            if (declaredViewFields.size() > 0) {
                viewFields.addAll(declaredViewFields);
            }
            superClass = superClass.getSuperclass();
        }
        return viewFields;
    }

    private List<Field> getDeclaredViewFields(Class<?> clazz) {
        List<Field> viewFields = new ArrayList();
        for (Field f : clazz.getDeclaredFields()) {
            if (View.class.isAssignableFrom(f.getType())) {
                viewFields.add(f);
            }
        }
        return viewFields;
    }

    private boolean isSaripaarAnnotation(Annotation annotation) {
        Class<?> annotationType = annotation.annotationType();
        return annotationType.equals(Checked.class) || annotationType.equals(ConfirmPassword.class) || annotationType.equals(Email.class) || annotationType.equals(IpAddress.class) || annotationType.equals(NumberRule.class) || annotationType.equals(Password.class) || annotationType.equals(Regex.class) || annotationType.equals(Required.class) || annotationType.equals(Select.class) || annotationType.equals(TextRule.class);
    }
}
