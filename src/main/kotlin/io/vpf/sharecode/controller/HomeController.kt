package io.vpf.sharecode.controller

import io.vpf.sharecode.persistence.SharecodeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView
import java.util.*
import javax.validation.Valid


@Controller
class HomeController @Autowired constructor(val repo: SharecodeRepository) {

    @RequestMapping(value = ["/"], method = [RequestMethod.GET])
    fun index(post: Post, model: Model): String {
        model.addAttribute("post", Post(UUID.randomUUID()))
        model.addAttribute("src", "")
        return "index"
    }

    @RequestMapping(value = ["/"], method = [RequestMethod.POST])
    fun addNewCode(@Valid post: Post, bindingResult: BindingResult, model: Model): ModelAndView {
        if (bindingResult.hasErrors()) {
            return ModelAndView("index")
        }
        repo.putCode(post.id!!, post.value ?: "")
        return ModelAndView("index", mapOf(
                "post" to post,
                "link" to "code/${post.id.toString()}",
                "qr" to "qr/${post.id.toString()}",
                "value" to "value/${post.id.toString()}"))
    }


}


open class Post(
        var id: UUID? = null,
        var value: String? = null
)