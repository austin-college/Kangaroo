<div class="textbook">

    <div class="photo">
        <g:if test="${textbook.imageUrl}">
            <a href="${textbook.toLink()}"><img src="${textbook.imageUrl}" height="90px"/></a>
        </g:if>
        <g:else>
            <span class="noBookImage"></span>

        </g:else>
    </div>

    <div style="margin-left: 100px; padding-top: 10px;">
        <div class="title">
            <a href="${textbook.toLink()}">${textbook.title}</a> <g:if
                test="${textbook.edition}">(${textbook.edition} edition)</g:if>
        </div>


        <div class="price">
            <g:if test="${textbook.matchedOnAmazon && textbook.amazonPrice < textbook.bookstoreNewPrice}">
                <div>
                    <span class="oldPrice">$${textbook.bookstoreNewPrice}</span>
                    <b>$${textbook.amazonPrice}</b>
                </div>
            </g:if>
            <g:else>
                <div><b>$${textbook.bookstoreNewPrice}</b></div>
            </g:else>
        </div>

        <g:if test="${textbook.author}">
            <div class="author">
                by ${textbook.author}
            </div>
        </g:if>
    </div>
</div>