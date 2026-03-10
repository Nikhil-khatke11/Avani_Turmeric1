// ============================================
// AVANI TURMERIC - ANIMATIONS & INTERACTIONS
// JavaScript for scroll effects and dynamic features
// ============================================

(function () {
    'use strict';

    // ============================================
    // PAGE LOADER
    // ============================================
    function initPageLoader() {
        window.addEventListener('load', () => {
            const loader = document.querySelector('.page-loader');
            if (loader) {
                setTimeout(() => {
                    loader.classList.add('hidden');
                    // Remove from DOM after transition
                    setTimeout(() => loader.remove(), 500);
                }, 800);
            }
        });
    }

    // ============================================
    // SCROLL ANIMATIONS - Intersection Observer
    // ============================================
    function initScrollAnimations() {
        const animatedElements = document.querySelectorAll(
            '.scroll-animate, .fade-in-left, .fade-in-right, .fade-in-up, .scale-in'
        );

        if (animatedElements.length === 0) return;

        const observerOptions = {
            root: null,
            rootMargin: '0px',
            threshold: 0.1
        };

        const observer = new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    entry.target.classList.add('show');
                    // Optional: unobserve after animation to improve performance
                    // observer.unobserve(entry.target);
                } else {
                    // Remove 'show' class when element leaves viewport for re-animation
                    entry.target.classList.remove('show');
                }
            });
        }, observerOptions);

        animatedElements.forEach(element => {
            observer.observe(element);
        });
    }

    // ============================================
    // NAVBAR SCROLL EFFECT
    // ============================================
    function initNavbarScroll() {
        const navbar = document.querySelector('.navbar');
        if (!navbar) return;

        let lastScroll = 0;

        function handleNavbarScroll() {
            const currentScroll = window.pageYOffset;

            if (currentScroll > 50) {
                navbar.classList.add('scrolled');
            } else {
                navbar.classList.remove('scrolled');
            }

            lastScroll = currentScroll;
        }

        // Debounce scroll event for better performance
        let ticking = false;
        window.addEventListener('scroll', () => {
            if (!ticking) {
                window.requestAnimationFrame(() => {
                    handleNavbarScroll();
                    ticking = false;
                });
                ticking = true;
            }
        });
    }

    // ============================================
    // BACK TO TOP BUTTON
    // ============================================
    function initBackToTop() {
        // Create button if it doesn't exist
        let backToTopBtn = document.querySelector('.back-to-top');

        if (!backToTopBtn) {
            backToTopBtn = document.createElement('button');
            backToTopBtn.className = 'back-to-top';
            backToTopBtn.innerHTML = '<i class="bi bi-arrow-up"></i>';
            backToTopBtn.setAttribute('aria-label', 'Back to top');
            document.body.appendChild(backToTopBtn);
        }

        // Show/hide based on scroll position
        function toggleBackToTop() {
            if (window.pageYOffset > 300) {
                backToTopBtn.classList.add('show');
            } else {
                backToTopBtn.classList.remove('show');
            }
        }

        // Smooth scroll to top
        backToTopBtn.addEventListener('click', () => {
            window.scrollTo({
                top: 0,
                behavior: 'smooth'
            });
        });

        // Debounced scroll listener
        let ticking = false;
        window.addEventListener('scroll', () => {
            if (!ticking) {
                window.requestAnimationFrame(() => {
                    toggleBackToTop();
                    ticking = false;
                });
                ticking = true;
            }
        });
    }

    // ============================================
    // PARALLAX EFFECT FOR HERO
    // ============================================
    function initParallax() {
        const hero = document.querySelector('.hero');
        if (!hero) return;

        function handleParallax() {
            const scrolled = window.pageYOffset;
            const parallaxSpeed = 0.5;
            hero.style.backgroundPositionY = `${scrolled * parallaxSpeed}px`;
        }

        let ticking = false;
        window.addEventListener('scroll', () => {
            if (!ticking) {
                window.requestAnimationFrame(() => {
                    handleParallax();
                    ticking = false;
                });
                ticking = true;
            }
        });
    }

    // ============================================
    // SMOOTH SCROLL FOR ANCHOR LINKS
    // ============================================
    function initSmoothScroll() {
        document.querySelectorAll('a[href^="#"]').forEach(anchor => {
            anchor.addEventListener('click', function (e) {
                const targetId = this.getAttribute('href');

                // Skip if it's just "#" or empty
                if (targetId === '#' || targetId === '#!') {
                    e.preventDefault();
                    return;
                }

                const targetElement = document.querySelector(targetId);
                if (targetElement) {
                    e.preventDefault();
                    targetElement.scrollIntoView({
                        behavior: 'smooth',
                        block: 'start'
                    });
                }
            });
        });
    }

    // ============================================
    // PRICE ANIMATION (for product page)
    // ============================================
    function initPriceAnimation() {
        const priceElement = document.getElementById('totalPrice');
        if (!priceElement) return;

        // Create a MutationObserver to watch for price changes
        const observer = new MutationObserver(() => {
            priceElement.parentElement.classList.add('updated');
            setTimeout(() => {
                priceElement.parentElement.classList.remove('updated');
            }, 400);
        });

        observer.observe(priceElement, {
            childList: true,
            characterData: true,
            subtree: true
        });
    }

    // ============================================
    // ADD RIPPLE EFFECT TO BUTTONS
    // ============================================
    function initButtonRipple() {
        const buttons = document.querySelectorAll('.btn');

        buttons.forEach(button => {
            button.addEventListener('click', function (e) {
                const ripple = document.createElement('span');
                const rect = this.getBoundingClientRect();
                const size = Math.max(rect.width, rect.height);
                const x = e.clientX - rect.left - size / 2;
                const y = e.clientY - rect.top - size / 2;

                ripple.style.width = ripple.style.height = size + 'px';
                ripple.style.left = x + 'px';
                ripple.style.top = y + 'px';
                ripple.classList.add('ripple');

                this.appendChild(ripple);

                setTimeout(() => ripple.remove(), 600);
            });
        });
    }

    // ============================================
    // STAGGER ANIMATION FOR LISTS
    // ============================================
    function initStaggerAnimations() {
        const staggerContainers = document.querySelectorAll('[data-stagger]');

        staggerContainers.forEach(container => {
            const children = container.children;
            Array.from(children).forEach((child, index) => {
                child.style.animationDelay = `${index * 0.1}s`;
                child.classList.add('fade-in-up');
            });
        });
    }

    // ============================================
    // LAZY LOAD IMAGES
    // ============================================
    function initLazyLoad() {
        const images = document.querySelectorAll('img[data-src]');

        if (images.length === 0) return;

        const imageObserver = new IntersectionObserver((entries, observer) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    const img = entry.target;
                    img.src = img.dataset.src;
                    img.removeAttribute('data-src');
                    observer.unobserve(img);
                }
            });
        });

        images.forEach(img => imageObserver.observe(img));
    }

    // ============================================
    // FORM INPUT ANIMATIONS
    // ============================================
    function initFormAnimations() {
        const inputs = document.querySelectorAll('.form-control');

        inputs.forEach(input => {
            input.addEventListener('focus', function () {
                this.parentElement.classList.add('input-focused');
            });

            input.addEventListener('blur', function () {
                this.parentElement.classList.remove('input-focused');
            });
        });
    }

    // ============================================
    // INITIALIZE ALL FEATURES
    // ============================================
    function init() {
        // Wait for DOM to be ready
        if (document.readyState === 'loading') {
            document.addEventListener('DOMContentLoaded', initAll);
        } else {
            initAll();
        }
    }

    function initAll() {
        initPageLoader();
        initScrollAnimations();
        initNavbarScroll();
        initBackToTop();
        initParallax();
        initSmoothScroll();
        initPriceAnimation();
        initButtonRipple();
        initStaggerAnimations();
        initLazyLoad();
        initFormAnimations();

        // Trigger initial scroll animations
        window.dispatchEvent(new Event('scroll'));
    }

    // Start initialization
    init();

})();

// ============================================
// UTILITY: Counter Animation (for statistics)
// ============================================
function animateCounter(element, target, duration = 2000) {
    const start = 0;
    const increment = target / (duration / 16); // 60fps
    let current = start;

    const timer = setInterval(() => {
        current += increment;
        if (current >= target) {
            element.textContent = target;
            clearInterval(timer);
        } else {
            element.textContent = Math.floor(current);
        }
    }, 16);
}

// ============================================
// EXPORT FOR EXTERNAL USE
// ============================================
window.AvaniAnimations = {
    animateCounter: animateCounter
};
