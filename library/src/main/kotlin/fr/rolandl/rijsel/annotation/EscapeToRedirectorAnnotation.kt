package fr.rolandl.rijsel.annotation

import java.lang.annotation.Inherited

/**
 * Annotation which should be used as a marker on a [fr.rolandl.rijsel.appcompat.app.RijselAppCompatActivity] or [fr.rolandl.rijsel.activity.RijselComposeActivity], which does not want to be requested by the
 * [fr.rolandl.rijsel.app.RijselActivityController.Redirector].
 *
 *
 * When a [fr.rolandl.rijsel.appcompat.app.RijselAppCompatActivity] or [fr.rolandl.rijsel.activity.RijselComposeActivity] uses this annotation, the [fr.rolandl.rijsel.app.RijselActivityController.Redirector.getRedirection]
 * ] method will not be
 * invoked.
 *
 *
 * @see fr.rolandl.rijsel.app.RijselActivityController.needsRedirection
 * @author Ludovic Roland
 * @since 2018.12.31
 */
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
@Inherited
annotation class EscapeToRedirectorAnnotation
